package xyz.erik.skuxx.mods.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.Render3D;
import xyz.erik.skuxx.event.events.RenderEvent;

public class Breaker extends Mod {


    private BlockPos breakingBlock;

    @EventTarget
    private void onUpdate(final EventMotion e) {

        if(e.getMotion() == Event.Motion.BEFORE) {

            int range = 4;
            Breaker.this.breakingBlock = null;
            for(int y = -range; y < range; y++) {
                for(int x = -range; x < range; x++) {
                  for (int z = -range; z < range; z++) {


                        BlockPos blockPos = new BlockPos(Math.floor(getPlayer().posX + x), Math.floor(getPlayer().posY + y),Math.floor(getPlayer().posZ + z));

                        if (Helper.getBlock(blockPos) instanceof BlockBed || Helper.getBlock(blockPos) instanceof BlockDragonEgg) {

                            Breaker.this.breakingBlock = blockPos;

                            break;
                        }
                    }
                }
            }

            if (breakingBlock != null) {

                    float[] rots = Helper.getBlockRotation(breakingBlock);
                    e.setRotationYaw(rots[0]);
                    e.setRotationPitch(rots[1]);

            }


        } else if(e.getMotion() == Event.Motion.AFTER) {
            if (breakingBlock != null) {
                if (getMinecraft().playerController.blockHitDelay > 0) {
                    getMinecraft().playerController.blockHitDelay = 0;
                }
                EnumFacing direction = getFacingDirection(this.breakingBlock);
                if (direction != null) {
                    if(getPlayerController().func_180512_c(this.breakingBlock, direction)) {
                         getPlayer().swingItem();
                    }
                }
            }
        }
    }
    @EventTarget
    private void render(Render3D event) {

        if (breakingBlock == null) return;

        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glDepthMask(false);
        GL11.glLineWidth(0.75F);

        GL11.glColor4f(0.2F, 1.2F, 1.2F, 1.0F);

        getMinecraft().getRenderManager();double x = this.breakingBlock.getX() - getMinecraft().renderManager.renderPosX;
        getMinecraft().getRenderManager();double y = this.breakingBlock.getY() - getMinecraft().renderManager.renderPosY;
        getMinecraft().getRenderManager();double z = this.breakingBlock.getZ() - getMinecraft().renderManager.renderPosZ;

        double xo = 1.0D;
        double yo = 1.0D;
        double zo = 1.0D;

        AxisAlignedBB mask = new AxisAlignedBB(x, y, z,
                x + xo - xo * getMinecraft().playerController.curBlockDamageMP,
                y + yo - yo * getMinecraft().playerController.curBlockDamageMP,
                z + zo - zo * getMinecraft().playerController.curBlockDamageMP);

        RenderUtil.drawOutlinedBoundingBox(mask);

        GL11.glColor4f(0.2F, 1.2F, 1.2F, 0.11F);

        RenderUtil.drawFilledBox(mask);

        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
    }
    private EnumFacing getFacingDirection(BlockPos pos)
    {
        EnumFacing[] orderedValues = { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
        for (EnumFacing facing : orderedValues)
        {
            Entity temp = new EntitySnowball(getWorld());
            temp.posX = (pos.getX() + 0.5D);
            temp.posY = (pos.getY() + 0.5D);
            temp.posZ = (pos.getZ() + 0.5D);
            temp.posX += facing.getDirectionVec().getX() * 0.5D;
            temp.posY += facing.getDirectionVec().getY() * 0.5D;
            temp.posZ += facing.getDirectionVec().getZ() * 0.5D;
            if (getPlayer().canEntityBeSeen(temp)) {
                return facing;
            }
        }
        return EnumFacing.DOWN;
    }
}
