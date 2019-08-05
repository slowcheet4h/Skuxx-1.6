package xyz.erik.skuxx.mods.combat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class AutoHeal
extends Mod
{

    private Mode mode = Mode.BOTH;
    private ErikTimer Timer = new ErikTimer();
    private boolean doNext;
    double  x,y,z;
    int ticks = 0;
    private Bool jump = new Bool("Jump",false);
    private Double delay = new Double("Delay",300,100,600);
    private Double health = new Double("Health",4.5D,1,10);

    public AutoHeal() {
        setCategory(Category.COMBAT);

        addSet(delay);
        addSet(jump);
        addSet(health);
    }
    @EventTarget
    private void onMotionUpdate(EventMotion event) {
        if (event.getMotion() == EventMotion.Motion.BEFORE) {
            int healerIndex = getHealItem();

            if (getPlayer().getHealth() > health.getValue()  * 2|| !Timer.delay((float)delay.getValue()) || (healerIndex == -2)) {
                return;
            }
            if (mode == Mode.POTION) {
                setRunning(2);
                event.setRotationPitch(90);
            }

            if (mode == Mode.BOTH) {
                Item item = getPlayer().inventory.mainInventory[healerIndex].getItem();
                if (item instanceof ItemPotion) {
                    setRunning(2);
                    Skuxx.getInstance().getModManager().getMod("Aura").setRunning(-3);
                    if(jump.isState() && getPlayer().onGround)
                        event.setRotationPitch(-90);
                    else
                    event.setRotationPitch(90);


                }
            }
            doNext = true;
        } else {
            int healerIndex = getHealItem();

            if (getPlayer().getHealth() > health.getValue() * 2 &&!Timer.delay((float)delay.getValue()) && (healerIndex == -2)) return;
            int nonUsedSlot = 8;
            if (doNext) {

                if (healerIndex < 9) {
                    if (getPlayer().inventory.currentItem != healerIndex)
                        getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(healerIndex));

                    getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getPlayer().getHeldItem()));
                    if (getPlayer().inventory.currentItem != healerIndex)
                    getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                } else {
                    getPlayer().swap(healerIndex,nonUsedSlot);
                    healerIndex = nonUsedSlot;
                    if (getPlayer().inventory.currentItem != nonUsedSlot)
                    getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(nonUsedSlot));

                    getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getPlayer().getHeldItem()));
                //so i say goodbye again!
                    if (getPlayer().inventory.currentItem != nonUsedSlot)
                        getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));

                }
                if (jump.isState() && getPlayer().onGround && getPlayer().getLastPitch() == -90) {
                    Item item = getPlayer().inventory.mainInventory[healerIndex].getItem();
                    if (item instanceof ItemPotion) {
                        getPlayer().jump();
                        if (Skuxx.getInstance().getModManager().getMod("Aura").getRunningTicks() >= 0)
                            Skuxx.getInstance().getModManager().getMod("Aura").setRunning(-1);

                        getPlayer().motionZ = 0;
                        getPlayer().motionX = 0;
                    }
                }


                doNext = false;
                Timer.reset();
            }

        }


    }



    private int getNonUsedSlot() {
        for(int i = 0; i <9; i++) {
            ItemStack stack = getPlayer().inventoryContainer.getSlot(i).getStack();
            if (stack == null || stack.getItem() == null) {
                return i;
            }
        }
        return 8;
    }


    private int getHealItem() {
        switch(mode) {
            case POTION:
                for(int index = 1; index < 45; index++) {
                    ItemStack itemStack = getPlayer().inventoryContainer.getSlot(index).getStack();
                    if ((itemStack != null) && (isStackSplashHealthPot(itemStack))) {
                        if (index > 36) {
                            index -= 36;
                        }
                        return index;
                    }
                }
                break;
            case SOUP:
                for(int index = 1; index < 45; index++) {
                    ItemStack itemStack = getPlayer().inventoryContainer.getSlot(index).getStack();
                    if ((itemStack != null) && (itemStack.getItem() == Items.mushroom_stew)) {
                        if (index > 36) {
                            index -= 36;
                        }
                        return index;
                    }
                }
                break;
                default:
                    for(int index = 1; index < 45; index++) {
                        ItemStack itemStack = getPlayer().inventoryContainer.getSlot(index).getStack();
                        if ((itemStack != null) && (isStackSplashHealthPot(itemStack))) {
                            if (index > 36) {
                                index -= 36;
                            }
                            return index;

                        }
                    }
                    for(int index = 1; index < 45; index++) {
                        ItemStack itemStack = getPlayer().inventoryContainer.getSlot(index).getStack();
                        if ((itemStack != null) && (itemStack.getItem() == Items.mushroom_stew)) {
                            if (index > 36) {
                                index -= 36;
                            }
                            return index;
                        }
                    }
                    break;
        }
        return -2;
    }



    public enum Mode {
        POTION,SOUP,BOTH;
    }

    private boolean isStackSplashHealthPot(ItemStack stack)
    {
        if (stack == null) {
            return false;
        }
        if ((stack.getItem() instanceof ItemPotion))
        {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if ((ItemPotion.isSplash(stack.getItemDamage())) && (potion.getEffects(stack) != null)) {
                for (Object o : potion.getEffects(stack))
                {
                    PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.heal.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public String setMode(String mode) {
        switch (mode.toLowerCase()) {
            case "potion":
                this.mode = Mode.POTION;
                break;
            case "both":
                this.mode = Mode.BOTH;
                break;
            case "soup":
                this.mode = Mode.SOUP;
                break;
        }
        return "AutoHeal mode changed to " + this.mode;
    }
}
