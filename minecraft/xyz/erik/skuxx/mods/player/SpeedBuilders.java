package xyz.erik.skuxx.mods.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.util.BlockPos;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;
import xyz.erik.skuxx.mods.move.Speed;

import java.util.ArrayList;
import java.util.List;

public class SpeedBuilders
extends Mod {
    public static BlockPos middlePos = null;

    int lastBlock = 0;
    public SpeedBuilders() {

        setCategory(Category.PLAYER);
    }

    public void onEnabled() {
        super.onEnabled();
        middlePos = null;
        currentBlocks = new ArrayList<>();
        currentBlocks.clear();
        if (getPlayer() == null) toggle();
        lastBlock = 0;


    }

    private ErikTimer erikTimer = new ErikTimer();
    private List<BlockPos> currentBlocks;


    @EventTarget
    private void onMotion(EventMotion e) {
        if (e.getMotion() == EventMotion.Motion.BEFORE) {
            if (middlePos != null) {

            //delay

                if (Helper.mc().gameSettings.keyBindPickBlock.pressed) {
                    currentBlocks = getBlocks(new BlockPos(middlePos.getX(), middlePos.getY() + 1, middlePos.getZ()));
                }

            if (!getPlayer().isSneaking() && Helper.mc().gameSettings.keyBindUseItem.pressed && erikTimer.delay(200) && currentBlocks.size() > 0) {
                // gets blocks
                BlockPos theBlock = currentBlocks.get(lastBlock);

                // gets item from block
//                autoGet(Item.getItemFromBlock(block));
                if(getSlot(theBlock) != -1) {
                    getPlayer().inventory.currentItem = getSlot(theBlock);
                    getPlayer().sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                }
                //looks to block place position
                e.setRotationYaw(Helper.getBlockRotation(theBlock)[0]);
                e.setRotationPitch(Helper.getBlockRotation(theBlock)[1]);

                getPlayer().rotationYawHead = e.getRotationYaw();

                //sends block placement packet
                getPlayer().swingItem();
                setRunning(1);
                getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(theBlock.getX(),theBlock.getY() - 1,theBlock.getZ()),1,getPlayer().getHeldItem(),middlePos.getX(),middlePos.getY() - 1,middlePos.getZ()));

                lastBlock++;
                if (lastBlock >= currentBlocks.size()) {
                    lastBlock = 0;
                    currentBlocks.clear();
                }
                //resets timer
                erikTimer.reset();
            }
            }
        }
    }

    public BlockPos getClosestBlock() {
        double DIST = 99;
        BlockPos pos = null;
        for(BlockPos blockPos : currentBlocks) {
            if (getPlayer().getDistance(blockPos) < DIST) {
                DIST = getPlayer().getDistance(blockPos);
                pos = blockPos;
            }
        }
        return pos;
    }


    @EventTarget
    private void onPacketSend(EventPacket event) {
        if (event.getType() == EventPacket.Type.SEND) {
            if (event.getPacket() instanceof C07PacketPlayerDigging) {
                C07PacketPlayerDigging blockBreaking = (C07PacketPlayerDigging)event.getPacket();
                if (blockBreaking.func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK && getPlayer().isSneaking()) {
                    if (middlePos == null) {
                        middlePos = blockBreaking.func_179715_a();
                    }
                }
            }
        }
    }


    private int getSlot(BlockPos block)
    {
        for (int i = 0; i < 9; i++) {
            if ((this.getPlayer().inventory.mainInventory[i] != null) && (this.getPlayer().inventory.mainInventory[i].getItem() != null) && ((this.getPlayer().inventory.mainInventory[i].getItem() == Item.getItemFromBlock(Helper.getBlock(block))))) {
                return i;
            }
        }
        return -1;
    }

    private BlockPos yourMiddle;

    private  List<BlockPos> getBlocks(BlockPos middlePos) {
        List<BlockPos> positions = new ArrayList<>();
        int range = 4;
        for(int x = -range; x < range; x++) {
            for(int y = 0; y < 12; y++) {
                for(int z = -range; z < range; z++) {
                    BlockPos block = new BlockPos(Math.floor(middlePos.getX() + x), Math.floor(middlePos.getY() + y), Math.floor(middlePos.getZ()+z));
                    if (Helper.getBlock(block) != Blocks.air) {
                        positions.add(block);
                    }
                }
            }
        }




        return positions;

    }
}











