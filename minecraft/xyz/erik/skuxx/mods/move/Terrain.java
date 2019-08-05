package xyz.erik.skuxx.mods.move;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

import java.util.Random;

public class Terrain
extends Mod{

    public Bool fast = new Bool("Ladders",true);
    public Bool motionSpider = new Bool("MotionSpider",false);
    public Bool ice = new Bool("ice",true);

    private ErikTimer timer = new ErikTimer();
    public Terrain(){
        addSet(fast);
        addSet(motionSpider);
        addSet(ice);
        setCategory(Category.MOVE);
    }

    @EventTarget
    private void onMotion(EventMotion e) {
        if (e.getMotion() == Event.Motion.BEFORE) {



                if (getPlayer().isOnLadder() && timer.delay(500) && !getPlayer().isSneaking() && fast.isState() && !is2BlockLadder()) {
                getMinecraft().getTimer().timerSpeed = 1.5F;
                timer.reset();
            }
            if (timer.delay(100) && fast.isState() && !timer.delay(500)) {
                getMinecraft().getTimer().timerSpeed = 1f;
            }
            if (ice.isState() && Helper.MovementInput()){
                Block block = getWorld().getBlockState(new BlockPos(getPlayer().posX,getPlayer().posY - 0.2,getPlayer().posZ)).getBlock();
                if (block instanceof BlockIce || block instanceof BlockPackedIce) {
                    getPlayer().setSpeed(0.7    );
                }
            }
        } 
    }


    private boolean is2BlockLadder() {
        boolean first = getWorld().getBlockState(new BlockPos(getPlayer().posX,getPlayer().posY + 1,getPlayer().posZ)).getBlock() == Blocks.air;
        boolean second = getWorld().getBlockState(new BlockPos(getPlayer().posX,getPlayer().posY + 2,getPlayer().posZ)).getBlock() instanceof BlockLadder || getWorld().getBlockState(new BlockPos(getPlayer().posX,getPlayer().posY + 2,getPlayer().posZ)).getBlock() instanceof BlockVine;

        return first && second;
    }


    public void onEnabled() {
        super.onEnabled();
    }

    public void onDisabled() {
        Blocks.ice.slipperiness = 0.98F;
        Blocks.packed_ice.slipperiness = 0.98F;

        super.onDisabled();
    }
}
