package xyz.erik.skuxx.mods.move;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.SlowlessEvent;
import xyz.erik.skuxx.event.events.WaterPushEvent;
import xyz.erik.skuxx.mods.Category;

import java.util.Random;


public class NoSlow
extends Mod
{

    private boolean ground;

    public NoSlow() {
        super("NoSlow",new String[]{"noslowdown","itemrun"});

        setCategory(Category.MOVE);
    }

    @EventTarget
    public void ItemRun(SlowlessEvent slowItem)
    {
        slowItem.setCancelled(true);
    }

    @EventTarget(4)
    private void onMotionUpdate(EventMotion eventMotion)
    {

       if (getPlayer().isBlocking() && (this.getPlayer().motionX != 0 || this.getPlayer().motionZ != 0) &&this.ground) {
           if (eventMotion.getMotion() == Event.Motion.BEFORE) {
                getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,BlockPos.ORIGIN,EnumFacing.DOWN));
           }
           else if(eventMotion.getMotion() == Event.Motion.AFTER) {
               getPlayer().sendPacket(new C08PacketPlayerBlockPlacement(getPlayer().getHeldItem()));
           }

       }
        this.ground = this.minecraft.thePlayer.onGround;
    }

    @EventTarget
    private void waterPush(WaterPushEvent event) {
        event.setCancelled(true);
    }
}
