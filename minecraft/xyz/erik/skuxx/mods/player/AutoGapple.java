package xyz.erik.skuxx.mods.player;

import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.mod.ModManager;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;

public class AutoGapple
extends Mod
{


    private int lastSlot;
    private boolean swapped;
    private int ticks;
    private Double health = new Double("Health",5.5D,1,20);
    private ErikTimer timer = new ErikTimer();
    private Int delay = new Int("delay",300,100,10000);

    public AutoGapple() {
        addSet(delay);
        addSet(health);
    }
    @EventTarget
    private void onUpdate(final EventMotion e) {
        if (e.getMotion() == Event.Motion.AFTER)  {

            if (getPlayer().getHealth() <= health.getValue() * 2D && this.getPlayer().onGround && !this.swapped && !getPlayer().isPotionActive(Potion.regeneration.id)) {
                int index = getGapple();
                if (index != -1) {

                    if (!swapped) {

                        getPlayer().sendPacket(new C09PacketHeldItemChange(this.lastSlot));
                        this.lastSlot = index;
                        this.swapped = true;
                        NotificationManager.addNotification(new Notification(index +"",false,15));
                        this.ticks = 0;
                    }
                    this.timer.reset();
            }
            }

            if (this.swapped) {
                this.getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.getPlayer().inventory.mainInventory[this.lastSlot]));
this.ticks = -1;
System.out.println("EATING");
            }


            if (this.swapped && getPlayer().isPotionActive(Potion.regeneration.id)) {


                getPlayer().sendPacket(new C09PacketHeldItemChange(this.getPlayer().inventory.currentItem));
                this.swapped = false;
            }
        }
    }




    public int getGapple() {
        for (int index = 1; index < 45; index++) {
            ItemStack itemStack = getPlayer().inventoryContainer.getSlot(index).getStack();
            if (((isGapple(itemStack)))) {
                if (index > 36) {
                    index -= 36;
                } else
                    continue;

                return index;
            }
        }
        return -1;
    }

    private boolean isGapple(ItemStack stack) {
        return stack != null && stack.getItem() != null && (stack.getItem() instanceof ItemAppleGold);
    }


}
