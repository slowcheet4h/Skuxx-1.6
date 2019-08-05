package xyz.erik.skuxx.mods.move;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.*;

public class VerticalPhase
        extends Mod{


    public void onEnabled() {
        super.onEnabled();
    }

    @EventTarget
    private void onUpdate(EventMotion e) {

    }

    private boolean nextTick;
    private boolean jump;

    @EventTarget
    private void onJump(OnJump e) {
        e.setCancelled(true);
        if (getPlayer().isSneaking()) {
            getPlayer().motionY = 0.4;
        } else {
            if (getPlayer().isInsideBlock()) {
                getPlayer().setPositionAndUpdate(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ);
            } else {

                int blocks = 1;

                for (int y = 2; y < 20; y++) {
                    BlockPos pos = new BlockPos(getPlayer().posX,getPlayer().posY - y ,getPlayer().posZ);
                    Block blok  = Helper.getBlock(pos);
                    if (!(blok instanceof BlockAir)) break;

                        blocks++;


                }
                if (blocks <= 1) {
                    for (int i = 3; i > 0; i--) {
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ, true));

                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY, getPlayer().posZ, false));
                    }
                    getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ, true));
                    getPlayer().setPositionAndUpdate(getPlayer().posX, getPlayer().posY - 1, getPlayer().posZ);
                    System.out.println("TEST");
                } else {
                    getPlayer().setPositionAndUpdate(getPlayer().posX, getPlayer().posY - blocks, getPlayer().posZ);
                }
            }
        }
    }


    @EventTarget
    private void packet(EventPacket e) {

        if (e.getType() == EventPacket.Type.TAKE) {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                //e.setCancelled(true);
            }
        }
    }

    @EventTarget
    private void onBB(BlockBoundingBoxEvent e) {
        if (e.getBlockPos().getY() >= getPlayer().posY && getPlayer().motionY  > 0) {
            e.setBoundingBox(null);

        }
    }


    @EventTarget
    private void pushOut(EventPush e) {
        e.setCancelled(true);
    }

}
