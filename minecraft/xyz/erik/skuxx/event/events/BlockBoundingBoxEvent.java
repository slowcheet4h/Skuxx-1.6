package xyz.erik.skuxx.event.events;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import xyz.erik.skuxx.event.Event;

public class BlockBoundingBoxEvent
        extends Event
{
    private IBlockState state;
    private Block block;
    private AxisAlignedBB boundingBox;
    private BlockPos blockPos;

    public BlockBoundingBoxEvent(Block block, AxisAlignedBB boundingBox, BlockPos blockPos, IBlockState state)
    {
        this.block = block;
        this.boundingBox = boundingBox;
        this.blockPos = blockPos;
        this.state = state;
    }

    public BlockBoundingBoxEvent(AxisAlignedBB var7, Block block, int x, int y, int z) {}

    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos)
    {
        this.blockPos = blockPos;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public void setBlock(Block block)
    {
        this.block = block;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox)
    {
        this.boundingBox = boundingBox;
    }

    public IBlockState getState()
    {
        return this.state;
    }
}
