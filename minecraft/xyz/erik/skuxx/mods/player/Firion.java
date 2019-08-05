package xyz.erik.skuxx.mods.player;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.List;

public class
Firion
extends Mod
{
    private Bool anti_fire = new Bool("Antifire",true);

    private Bool antipotion = new Bool("Antipotion",true);
    public Firion() {
        addSet(anti_fire);
        addSet(antipotion);
        setSuffix("Packet");
        setCategory(Category.PLAYER);
        removeables = new int[]{Potion.blindness.id,Potion.confusion.id,Potion.moveSlowdown.id,Potion.digSlowdown.id};
    }


    int[] removeables = new int[4];
    private boolean next;
    private int ignorePackets;
    private List<Packet> storedPackets = new ArrayList<>();
    private ErikTimer timer = new ErikTimer();
    private ErikTimer timer2 = new ErikTimer();
    @EventTarget
    private void onMotion(EventMotion event) {
        if (event.getMotion() == Event.Motion.BEFORE) {



            if (getPlayer().isInWater() && canSave()) {
                event.setCancelled(true);
            }
            if (antipotion.isState()) {
                Potion[] arrayOfPotion;
                int j = (arrayOfPotion = Potion.potionTypes).length;
                for (int i = 0; i < j; i++) {
                    Potion potion = arrayOfPotion[i];
                    if ((potion != null) && (potion.isBadEffect())) {
                        PotionEffect effect = getPlayer()
                                .getActivePotionEffect(potion);
                        if (effect != null) {
                            boolean contains = false;
                            for (int r : removeables) {
                                if(r == effect.getPotionID()) {
                                    getPlayer().removePotionEffect(effect.getPotionID());
                                    contains = true;
                                }
                            }

                            if (contains) continue;


                            if (timer.delay(1000) && effect.getDuration() < 5000 && getPlayer().onGround) {
                                for(int x = 0; x < 20; x++) {
                                    getPlayer().sendPacket(new C03PacketPlayer(getPlayer().onGround));
                                    effect.onUpdate(getPlayer());
                                }

                                timer.reset();
                            }
                        }
                    }
                }
            }
            if (anti_fire.isState()) {
                if (getPlayer().isBurning() && !getPlayer().isOnLiquid() && !getPlayer().isInLiquid()) {
                    if (storedPackets.size() >= 2) {
                        for(Packet p : storedPackets) {
                            getPlayer().sendPacket(p);
                        }
                        ignorePackets = -2;
                    }
                }
            }




        }
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (ignorePackets == 0 && getPlayer().isBurning() && !getPlayer().isOnLiquid() &&!getPlayer().isInLiquid() && anti_fire.isState()) {
                if (storedPackets.size()  < 2) {
                    storedPackets.add(event.getPacket());
                    System.out.println("TEST");
                    event.setCancelled(true);
                }
            } else {
                ignorePackets++;
            }
        }
    }

    private boolean canSave() {
        return !getPlayer().isSwingInProgress && getPlayer().motionX == 0 && getPlayer().motionZ == 0 && (getPlayer().motionY == -0.02 || getPlayer().motionY == 0);

    }
}
