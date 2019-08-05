package xyz.erik.skuxx.mods.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.SafeWalkEvent;
import xyz.erik.skuxx.mods.Category;

import java.util.Arrays;
import java.util.List;

public class Scaffold
extends Mod{
    private Bool swing = new Bool("Swing",false);
    private List<Block> blacklist = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava });

    private ErikTimer timer = new ErikTimer();
    private ErikTimer jumpTimer = new ErikTimer();
    BlockData blockData;
    private int index = -1;



    public Scaffold() {
        addSet(swing);

        setCategory(Category.PLAYER);
    }
    @EventTarget(2)
    private void onMotion(EventMotion event){
        if (event.getMotion() == Event.Motion.BEFORE) {
            getPlayer().distanceWalkedModified = 0.0F;
            this.blockData = null;
            this.index = getBlockIndex();
            if (index != -1 && !getPlayer().isSneaking()) {
                double x2 = Math.cos(Math.toRadians(this.getPlayer().rotationYaw + 90.0F));
                double z2 = Math.sin(Math.toRadians(this.getPlayer().rotationYaw + 90.0F));
                double xOffset = getPlayer().movementInput.moveForward * 0.4D * x2 + getPlayer().movementInput.moveStrafe * 0.4D * z2;
                double zOffset = getPlayer().movementInput.moveForward * 0.4D * z2 - getPlayer().movementInput.moveStrafe * 0.4D * x2;
                double x = this.getPlayer().posX + xOffset;double y = this.getPlayer().posY - 1.0D;double z = this.getPlayer()  .posZ + zOffset;
                BlockPos blockBelow = new BlockPos(x, y, z);


                if ( getWorld().getBlockState(blockBelow).getBlock() == Blocks.air) {
                    this.blockData = getBlockData(blockBelow);
                    EntitySnowball temp = new EntitySnowball(getWorld());
                    temp.setPosition(blockData.position.getX(),blockData.position.getY(),blockData.position.getZ());
                    float[] rots = Helper.getRotationsAtLocation(Location.CHEST,temp);
                    event.setRotationYaw(rots[0]);
                    event.setRotationPitch(rots[1]);

                }

            }
        } else if(event.getMotion() == Event.Motion.AFTER) {
            if (blockData != null) {

                if (timer.delay(Skuxx.getInstance().getModManager().getMod("Speed").getState() ? 100 : 75)) {
                    boolean cItem = index != getPlayer().inventory.currentItem;
                    int old = getPlayer().inventory.currentItem;
                    if (cItem) {
                        getPlayer().inventory.currentItem = index;
                        getPlayer().sendPacket(new C09PacketHeldItemChange(index));
                    }
                    Minecraft.getMinecraft().rightClickDelayTimer = 3;
                    if (getPlayerController().func_178890_a(getPlayer(), getWorld(), getPlayer().getHeldItem(), this.blockData.position, this.blockData.face, new Vec3(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ())));
                    if (swing.isState() && !getPlayer().isSwingInProgress)

                    getPlayer().swingItem();
                    else
                        getPlayer().sendPacket(new C0APacketAnimation());

                    if (cItem) {

                        getPlayer().inventory.currentItem = old;
                        getPlayer().sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                    }
                timer.reset();
                }

            }
        }
    }


    @EventTarget
    private void safeWalk(SafeWalkEvent e) {
       e.setSafeWalk(getPlayer().onGround || getMinecraft().gameSettings.keyBindJump.pressed);
    }
    private int getBlockIndex() {
        for (int i = 36; i < 45; i++)
        {
            ItemStack itemStack = this.getPlayer().inventoryContainer.getSlot(i).getStack();
            if ((itemStack != null) && ((itemStack.getItem() instanceof ItemBlock))) {
                return i - 36;
            }
        }
        return -1;
    }



    public BlockData getBlockData(BlockPos pos)
    {
        if (!blacklist.contains(minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!blacklist.contains(minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!blacklist.contains(minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!blacklist.contains(minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!blacklist.contains(minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    public class BlockData
    {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face)
        {
            this.position = position;
            this.face = face;
        }
    }


}
