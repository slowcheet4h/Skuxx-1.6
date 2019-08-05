package xyz.erik.skuxx.mods.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.RenderEvent;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Nuker
extends Mod
{


    public Nuker() {

        //setCategory(Category.PLAYER);

    }




    private List<BlockPos> blockList = new ArrayList<>();
    private int index;
    private ErikTimer timer = new ErikTimer();
    private BlockPos current;
    @EventTarget
    private void onMotion(EventMotion e) {
        if (e.getMotion() == EventMotion.Motion.BEFORE) {

            if(current == null || getWorld().getBlockState(current).getBlock() instanceof BlockAir) {
                this.current = getCurrent();
timer.reset();
            }

                if (current == null || getWorld().getBlockState(current).getBlock() instanceof BlockAir || getPlayer().getDistance(current) > getPlayerController().getBlockReachDistance() - 0.5) {
                    current = getCurrent();
                    timer.reset();


                }



                if (current == null) return;
                EntitySnowball temp = new EntitySnowball(getWorld());
                temp.setPosition(current.getX(),current.getY(),current.getZ());
                float[] val = Helper.getRotationsAtLocation(Location.CHEST,temp);
                e.setRotationYaw(val[0]);
                e.setRotationPitch(val[1]);

        } else if (e.getMotion() == Event.Motion.AFTER) {
            if (current == null) return;
            if (getWorld().getBlockState(current).getBlock()     instanceof BlockAir) {
                return;
            }

            if (timer.delay(120)) {

                if (getMinecraft().playerController.blockHitDelay > 0) {
                    getMinecraft().playerController.blockHitDelay = 0;
                }


                EnumFacing direction = getFacingDirection(this.current);
                if (direction != null) {
                    if(getPlayerController().func_180512_c(this.current, direction)) {
                        getPlayer().swingItem();
                    }
                }


            }

        }
    }


    @EventTarget
    private void render(RenderEvent event) {
        if (current == null) return;
        RenderUtil.drawLines(new AxisAlignedBB(current.getX() - 0.5,current.getY() - 0.5,current.getZ() - 0.5,current.getX() + 0.5,current.getY() + 0.5,current.getZ() + 0.5));
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
        return null;
    }



    public BlockPos getCurrent() {
     int radius = 5;
     double closest = radius + 1;
     BlockPos pos = null;
        for (int y = radius; y >= -3; y--) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }

                    BlockPos p = new BlockPos(Math.floor(getPlayer().posX + x),Math.floor(getPlayer().posY + y),Math.floor(getPlayer().posZ + z));
                    if (getPlayer().getDistance(p) > getPlayerController().getBlockReachDistance() - 0.5F) continue;
                    if(getFacingDirection(p) == null) continue;
                    if (getWorld().getBlockState(p).getBlock() != Blocks.air) {
                        if (getPlayer().getDistance(p) < closest) {
                            closest = getPlayer().getDistance(p);
                            pos = p;
                        }
                    }

                }
            }
        }
        if (pos == null) {
            System.out.println("TEST");
            pos =new BlockPos( Math.floor(getPlayer().posX),Math.floor(getPlayer().posY - 1),Math.floor(getPlayer().posZ));

        }


        return pos;
    }


    private List<BlockPos> getBlocks() {
        List<BlockPos> blocks = new ArrayList<>();
        blocks.clear();
        blockList.clear();
        byte radius = 5;

        for (int y = radius; y >= -radius; y--) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {

                    BlockPos bPos = new BlockPos(Math.floor(getPlayer().posX + x), Math.floor(getPlayer().posY + y),Math.floor(getPlayer().posZ + z));
                    Entity ent = new EntitySnowball(getWorld());
                    ent.setPositionAndUpdate(bPos.getX(),bPos.getY(),bPos.getZ());
                    boolean cansee = getPlayer().canEntityBeSeen(ent);
                    if (getPlayer().getDistance(bPos) > getPlayerController().getBlockReachDistance() - 0.5 || !cansee) continue;
                    if (!(getWorld().getBlockState(bPos).getBlock() instanceof BlockAir)) {
                        blocks.add(bPos);
                    }

                }
            }
        }

        System.out.println(blocks.size());
        if (blocks.size() == 0) {
            BlockPos blockBellow = new BlockPos(getPlayer().posX,getPlayer().posY - 1,getPlayer().posZ);
            if (getWorld().getBlockState(blockBellow).getBlock() != Blocks.air) {
                blocks.add(blockBellow);
                index = 0;
            }

        }

        System.out.println(blocks.size());

        return blocks;
    }


}
