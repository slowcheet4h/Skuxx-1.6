    package xyz.erik.skuxx.mods.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.*;
import xyz.erik.skuxx.mods.Category;

public class NoClip
extends Mod {
    private ErikTimer time = new ErikTimer();

    public NoClip() {
        this.aliases = new String[]{"phase", "ncpphase"};
        setCategory(Category.PLAYER);
    }


    @EventTarget
    private void onEntityCollision(BlockBoundingBoxEvent event) {
        if (getPlayer().isSneaking())
       event.setBoundingBox(null);

    }
    @EventTarget
    private void EventPush(EventPush eventPush) {
        eventPush.setCancelled(true);
    }

    public boolean isInsideBlock()
    {
        return getPlayer().isInsideBlock();
    }



    @EventTarget
    private void onMotion(EventMotion e) {
        if (e.getMotion() == Event.Motion.AFTER && (this.getPlayer().isCollidedHorizontally)) {

        }
    }
}
