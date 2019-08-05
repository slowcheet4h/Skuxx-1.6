package xyz.erik.skuxx.mods.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

public class Speedmine
extends Mod
{
    private Bool instant = new Bool("Instant",false);
    public Speedmine() {
        setCategory(Category.PLAYER);
        addSet(instant);
    }

    private BlockPos block;
    private EnumFacing facing;
    @EventTarget
    private void onPacket(EventPacket eventPacket) {

        if (eventPacket.getType() == EventPacket.Type.SEND ) {
        if (eventPacket.getPacket() instanceof C03PacketPlayer) {
            if(instant.isState()) return;
            getPlayerController().blockHitDelay = 0;
            if (getPlayerController().curBlockDamageMP >= 0.8) {
               getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block,facing));
            }

        }


        }

    }
}
