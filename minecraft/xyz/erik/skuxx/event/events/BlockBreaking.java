package xyz.erik.skuxx.event.events;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.erik.skuxx.event.Event;

public class BlockBreaking
extends Event{


    private Block block;
    private BlockPos pos;
    private EnumFacing facing;
    private Event.Motion motion;
    public BlockBreaking(Block block, BlockPos pos, EnumFacing enumFacing,Event.Motion motion) {
        this.block = block;
        this.pos = pos;
        this.facing = enumFacing;
        this.motion = motion;
    }


    public Block getBlock() {
        return block;
    }

    public EnumFacing getFacing() {
        return facing;
    }


    public Motion getMotion() {
        return motion;
    }

    public BlockPos getPos() {
        return pos;
    }
}
