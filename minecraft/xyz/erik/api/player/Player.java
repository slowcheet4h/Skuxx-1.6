package xyz.erik.api.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.EventPlayerMove;
import xyz.erik.skuxx.event.events.EventTick;
import xyz.erik.skuxx.event.events.SendingMessageEvent;
import xyz.erik.skuxx.irc.irccmd.CapePlayer;

import java.util.ArrayList;
import java.util.List;


public class Player
extends EntityPlayerSP {
    private double lastYaw;
    private double lastPitch;
    private boolean upsideDown;
    public boolean registered;

    public Player(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_) {
        super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
        if (!this.registered) {
            EventManager.register(this);
            registered = true;
        }
    }


    public boolean isUsingSword() {
        return getHeldItem() != null && getHeldItem().getItem() != null && getHeldItem().getItem() instanceof ItemSword;
    }

    public void setMoveSpeed(final EventPlayerMove event, final double speed) {
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public boolean isInsideBlock()
    {
        for (int x = MathHelper.floor_double(
                getEntityBoundingBox().minX);
             x < MathHelper.floor_double(
                 getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(
                   getEntityBoundingBox().minY);
                 y <
                         MathHelper.floor_double(getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(
                   getEntityBoundingBox().minZ);
                     z <
                             MathHelper.floor_double(getEntityBoundingBox().maxZ) + 1; z++)
                {
                    Block block = Helper.mc().theWorld
                            .getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null)
                    {
                        AxisAlignedBB boundingBox = block
                                .getCollisionBoundingBox(
                                        Helper.mc().theWorld,
                                        new BlockPos(x, y, z),
                                        Helper.mc().theWorld.getBlockState(
                                                new BlockPos(x, y, z)));
                        if (boundingBox != null) {
                            if (this.getEntityBoundingBox().intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isUpsideDown() {
        return upsideDown;
    }

    public boolean MovementInput() {
        return Helper.mc().gameSettings.keyBindForward.getIsKeyPressed() || Helper.mc().gameSettings.keyBindBack.getIsKeyPressed() || Helper.mc().gameSettings.keyBindLeft.getIsKeyPressed() || Helper.mc().gameSettings.keyBindRight.getIsKeyPressed() || Helper.mc().gameSettings.keyBindSneak.getIsKeyPressed();
    }

    public void setSprinting(boolean sprinting)
    {
        super.setSprinting  (sprinting);
    }



    public void addPotionEffect(Potion potion, int duration, int value) {
        this.addPotionEffect(new PotionEffect(potion.id, duration, value));
    }

    public void addPotionEffect(Potion potion, int duration, int value, boolean particle) {
        this.addPotionEffect(new PotionEffect(potion.id, duration, value, false, particle));
    }

    public void sendChatMessage(String p_71165_1_) {
        SendingMessageEvent mes = new SendingMessageEvent(p_71165_1_);
        mes.fire();
        if (mes.isCancelled()) {
            return;
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(mes.getMessage()));
    }



    public boolean isOnGround()
    {
        int y = (int)mc.thePlayer.boundingBox.copy().offset(0.0D, -0.1D, 0.0D).minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; z++)
            {
                Block block = Helper.mc().theWorld.getBlockState(new BlockPos(x,y,z)).getBlock();
                if ((block != null) && (!(block instanceof BlockAir))) {
                    return true;
                }
            }
        }
        return false;
    }


    public void logout() {
        this.sendQueue.addToSendQueue(new C02PacketUseEntity(this, C02PacketUseEntity.Action.ATTACK));
        this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX + Float.NEGATIVE_INFINITY, this.posY + 55, this.posZ, this.onGround));
    }

    public void swingwout() {
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;

            if (this.worldObj instanceof WorldServer) {
                ((WorldServer) this.worldObj).getEntityTracker().sendToAllTrackingEntity(this, new S0BPacketAnimation(this, 0));
            }
        }
    }


    ///{0,0.42,0.75,1,1.16,1.081599998,0.9263,0.69584,0.14};
    public int getItem(int id)
    {
        for (int index = 9; index < 45; index++)
        {
            ItemStack item = inventoryContainer.getSlot(index).getStack();
            if ((item != null) && (Item.getIdFromItem(item.getItem()) == id)) {
                return index;
            }
        }
        return -1;
    }

    public float getDirection()
    {
        float yaw = this.rotationYaw;float forward = this.moveForward;float strafe = this.moveStrafing;
        yaw += (forward < 0.0F ? 180 : 0);
        if (strafe < 0.0F) {
            yaw += (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        if (strafe > 0.0F) {
            yaw -= (forward < 0.0F ? -45 : forward == 0.0F ? 90 : 45);
        }
        return yaw * 0.017453292F;
    }

    public double getSpeed()
    {
        //TODO: MAYBE NTO WKROK

        return  (float)Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    }


    public double getDistance(BlockPos pos)
    {
        double var7 = this.posX - pos.getX();
        double var9 = this.posY - pos.getY();
        double var11 = this.posZ - pos.getZ();
        return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }

    public void setSpeed(double speed)
    {
        this.motionX = (-MathHelper.sin(getDirection()) * speed);
        this.motionZ = (MathHelper.cos(getDirection()) * speed);
    }

    public boolean isInLiquid(double offset)
    {
        return Helper.getBlockBelowPlayer(-offset) instanceof BlockLiquid;
    }

    public boolean isInLiquid()
    {

        boolean inLiquid = false;
        int y = (int)getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(this.getEntityBoundingBox().minX); x < MathHelper.floor_double(this.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(this.getEntityBoundingBox().minZ); z < MathHelper.floor_double(this.getEntityBoundingBox().maxZ) + 1; z++)
            {
                Block block = Helper.getBlock(x,y,z);
                if ((block != null) && (!(block instanceof BlockAir)))
                {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    if ((block instanceof BlockLiquid)) {
                        return true;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }


    public boolean isOnLiquid()
    {
        AxisAlignedBB boundingBox = getEntityBoundingBox();
        boundingBox = boundingBox.contract(0.0D, 0.0D, 0.0D).offset(0.0D, -0.02D, 0.0D);
        boolean onLiquid = false;
        int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0D); x++) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0D); z++)
            {
                Block block = Helper.getBlock(x, y, z);
                if (block != Blocks.air)
                {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }






    public void swap(int item, int toItem) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, item, toItem, 2, this.mc.thePlayer);
    }

    public void swapfuckgoback(int item,int toItem) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, item, toItem, 2, this.mc.thePlayer);
        this.mc.playerController.windowClickNoPacket(this.mc.thePlayer.inventoryContainer.windowId, item, toItem, 2, this.mc.thePlayer);

    }



    public void attack(Entity entity) {
        this.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
    }

    public void sendPacket(Packet packet) {
        this.sendQueue.addToSendQueue(packet);
    }

    @EventTarget
    private void packet(EventPacket event) {

        if (event.type == EventPacket.Type.TAKE) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)event.getPacket();
                s08PacketPlayerPosLook.setRotationYaw(rotationYaw);
                s08PacketPlayerPosLook.setRotationPitch(rotationPitch);
            }
        } else {
            if (event.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer packetPlayer = (C03PacketPlayer)event.getPacket();
                if (packetPlayer.getRotating()) {
                    lastYaw = packetPlayer.getYaw();
                    lastPitch = packetPlayer.getPitch();
                }

           /*     if (!capePlayerList.isEmpty()) {
                for(Object o : Helper.mc().theWorld.loadedEntityList) {
                    if (o instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) o;
                        for (CapePlayer capePlayer : capePlayerList) {
                            if (capePlayer.username.toLowerCase().equalsIgnoreCase(player.getName().toLowerCase())) {
                                    player.cape = capePlayer.cape;
                                    capePlayerList.remove(capePlayer);
                            }
                        }
                    }
                    }
                }*/
            }
        }
    }


    public double getLastPitch() {
        return lastPitch;
    }

    public double getLastYaw() {
        return lastYaw;
    }


}


