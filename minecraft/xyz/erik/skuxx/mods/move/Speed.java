package xyz.erik.skuxx.mods.move;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import optifine.MathUtils;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPlayerMove;
import xyz.erik.skuxx.mods.Category;
import xyz.erik.skuxx.mods.combat.Criticals;
import xyz.erik.skuxx.mods.move.speedmodes.Erik;

import java.util.List;

public class Speed
extends Mod
{
    private Mode mode = Mode.MEMEZ;
    public static Minecraft mc;
    public static int ticks;
    private double save;
    private boolean stop;
    public static boolean boost = false;

    private double speed;
    private int level;
    private boolean disabling;
    private boolean stopMotionUntilNext;
    public static double moveSpeed;
    private boolean spedUp;
    public static boolean canStep;
    private double lastDist;
    public static double yOffset;
    private boolean cancel;
    private boolean speedTick;
    private float speedTimer;
    private int timerDelay;



    public Speed() {
        this.speed = 6.0D;
        this.level = 1;
        moveSpeed = 0.2873D;
        this.speedTimer = 1.3F;
        this.modes = new String[]{"Memez","Onground","NCP"};
        setCategory(Category.MOVE);
        this.mc = Helper.mc();

    }

    public void onEnabled() {
        if (getPlayer() != null)
        moveSpeed = getBaseMoveSpeed();
        Minecraft.getMinecraft().getTimer().timerSpeed = 1.0F;
        this.cancel = false;
        this.level = 4;
        mode = Mode.NCP;
        this.lastDist = 0;

        super.onEnabled();
    }



    public static boolean isMoving(Entity e)
    {
        return (e.motionX != 0.0D) && (e.motionZ != 0.0D) && ((e.motionY != 0.0D) || (e.motionY > 0.0D));
    }


    @EventTarget
        private void onMoiton(EventMotion event) {
        if (event.getMotion() == Event.Motion.BEFORE) {
            if (mode == Mode.MEMEZ) {
                setSuffix("Memez");
                if ((getPlayer().MovementInput()) && (event.getMotion() == Event.Motion.BEFORE) && (!getPlayer().movementInput.jump) && (!getPlayer().isInWater())) {
                    if (Skuxx.getInstance().getModManager().getMod("Step").isRunning() || getPlayer().isOnLiquid())
                        return;
                    Criticals.setCancel(true);
                    boolean hack = getPlayer().ticksExisted % 2 == 0;
                    if (getPlayer().onGround) {
                        if (!hack) {
                            double offset = getWorld().getBlockState(new BlockPos(getPlayer()).add(0, 2, 0)).getBlock() == Blocks.air ? 0.4D : 0.2D;
                            event.setY(event.getY() + offset);
                            event.setGround(false);
                        }
                        getPlayer().setSpeed(getPlayer().getSpeed() * (hack ? 3.29D : 0.7058D));

                    }
                }
            } else if (mode == Mode.ONGROUND) {
                setSuffix("Onground");
            } else if (mode == Mode.NCP) {
               setSuffix("NCP");
                if (getPlayer().movementInput.jump) {
                    return;
                }
                if (Skuxx.getInstance().getModManager().getMod("Step").isRunning())
                    return;

                if (!getPlayer().onGround) return;
                if (!Helper.MovementInput() || getPlayer().isInWater() || getPlayer().isOnLiquid() || getPlayer().isInLiquid()) return;
                level++;
                double offset = getWorld().getBlockState(new BlockPos(getPlayer()).add(0, 2, 0)).getBlock() == Blocks.air ? 0.07 : 0.16D;

                switch (level) {
                    case 0:

                        if (offset == 0.16) {

                         getPlayer().setSpeed(getPlayer().getSpeed() * 2.5);
                            return;
                        }
                        event.setY(getPlayer().posY + offset);
                        event.setGround(false);


                        getMinecraft().gameSettings.keyBindJump.pressed = false;

                        getPlayer().setSpeed(getPlayer().getSpeed() * 0.7058D);

                         break;
                    case 1:
                        if (offset == 0.16) {
                            getPlayer().setSpeed(getPlayer().getSpeed());
                            return;
                        }
                        getPlayer().setSpeed(getPlayer().getSpeed() * 1.924);


                        break;
                }

                if (level >= 3) level = -1;




            }
            boolean moving = (Speed.mc.gameSettings.keyBindForward.getIsKeyPressed()) || (Speed.mc.gameSettings.keyBindLeft.getIsKeyPressed()) || (Speed.mc.gameSettings.keyBindRight.getIsKeyPressed()) || (Speed.mc.gameSettings.keyBindBack.getIsKeyPressed());
            if ((!moving) && (Speed.mc.thePlayer.onGround)) {
                Speed.this.cooldownHops = 2;
                Speed.this.moveSpeed *= 1.0800000429153442D;
                Speed.this.level = 2;
            }
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    private int cooldownHops;
    @EventTarget
    private void onMove(EventPlayerMove event) {
        boolean jumping = mc.gameSettings.keyBindJump.getIsKeyPressed();
        boolean flying = mc.thePlayer.capabilities.isFlying;
        boolean moving = (mc.gameSettings.keyBindForward.getIsKeyPressed()) || (mc.gameSettings.keyBindLeft.getIsKeyPressed()) || (mc.gameSettings.keyBindRight.getIsKeyPressed()) || (mc.gameSettings.keyBindBack.getIsKeyPressed());
        Block blockUnder = getWorld().getBlockState(new BlockPos( getPlayer().posX, getPlayer().posY -0.01F, getPlayer().posZ)).getBlock();
        boolean onLiquid = (blockUnder != null) && ((blockUnder instanceof BlockLiquid));
        Block blockIn = getWorld().getBlockState(new BlockPos(getPlayer().posX, getPlayer().posY + 0.2F, getPlayer().posZ)).getBlock();
        boolean inLiquid = (blockIn != null) && ((blockIn instanceof BlockLiquid));
        boolean inBlock = mc.thePlayer.isEntityInsideOpaqueBlock();
        boolean onLadder = mc.thePlayer.isOnLadder();
        boolean isCollided = mc.thePlayer.isCollidedHorizontally;

        float moveForward = getPlayer().movementInput.moveForward;
        float moveStrafe = getPlayer().movementInput.moveStrafe;
        float rotationYaw = mc.thePlayer.rotationYaw;
        if ((flying) || (inLiquid) || (onLiquid) || (onLadder) || (inBlock) || (isCollided))
        {
            this.moveSpeed = 0.0D;
            this.level = -6;
            return;
        }



        if (mode != Mode.ONGROUND) return;
        boolean old=getMinecraft().gameSettings.viewBobbing;

        Criticals.setCancel(true);
        getMinecraft().gameSettings.viewBobbing = false;
        if (mc.thePlayer.onGround) {
            ticks = 2;
        }
        if (MathUtils.round(mc.thePlayer.posY - (int)mc.thePlayer.posY, 3) == MathUtils.round(0.138D, 3))
        {
            mc.thePlayer.motionY -= 0.08D;
            event.setY(event.getY() - 0.09316090325960147D);
            mc.thePlayer.posY -= 0.09316090325960147D;
        }
        if ((ticks == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)))
        {
            ticks = 2;
            moveSpeed = 1.35D * getBaseMoveSpeed() - 0.01D;
        }
        else if (ticks == 2)
        {
            if (this.stop)
            {
                moveSpeed = 0.35D;
                this.stop = false;
            }
            moveSpeed *= 0.9D;
            ticks = 3;
            if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
                if (!mc.thePlayer.isCollidedHorizontally)
                {
                    mc.thePlayer.motionY = 0.399399995003033D;
                    event.setY(0.399399995003033D);
                    moveSpeed *= (boost ? 2.4D : 2.385D);
                    mc.thePlayer.motionY *= -5.0D;
                    boost = false;
                }
            }
        }
        else if (ticks == 3)
        {
            ticks = 4;
            double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
            moveSpeed = this.lastDist - difference;
        }
        else if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0) || (mc.thePlayer.isCollidedVertically))
        {
            ticks = 1;
        }
        moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());

        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if ((forward == 0.0F) && (strafe == 0.0F))
        {
            event.setX(0.0D);
            event.setZ(0.0D);
        }
        else if (forward != 0.0F)
        {
            if (strafe >= 1.0F)
            {
                yaw += (forward > 0.0F ? -45 : 45);
                strafe = 0.0F;
            }
            else if (strafe <= -1.0F)
            {
                yaw += (forward > 0.0F ? 45 : -45);
                strafe = 0.0F;
            }
            if (forward > 0.0F) {
                forward = 1.0F;
            } else if (forward < 0.0F) {
                forward = -1.0F;
            }
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0F));
        double mz = Math.sin(Math.toRadians(yaw + 90.0F));
        double motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        double motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;

        event.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        event.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);

        canStep = true;
        mc.thePlayer.stepHeight = 0.6F;
        if ((forward == 0.0F) && (strafe == 0.0F))
        {
            event.setX(0.0D);
            event.setZ(0.0D);
        }
        else {
            boolean collideCheck = false;
            if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.expand(0.5D, 0.0D, 0.5D)).size() > 0) {
                collideCheck = true;
            }
            if (forward != 0.0F) {
                if (strafe >= 1.0F) {
                    yaw += (forward > 0.0F ? -45 : 45);
                    strafe = 0.0F;
                } else if (strafe <= -1.0F) {
                    yaw += (forward > 0.0F ? 45 : -45);
                    strafe = 0.0F;
                }
                if (forward > 0.0F) {
                    forward = 1.0F;
                } else if (forward < 0.0F) {
                    forward = -1.0F;
                }
            }
        }

        getMinecraft().gameSettings.viewBobbing = old;
    }

    private double getBaseMoveSpeed()
    {
        double baseSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
        {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }



    public boolean getBlock(AxisAlignedBB bb)
    {
        int y = (int)bb.minY;
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++)
            {
                Block block = getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                if ((block != null) &&
                        (block.getCollisionBoundingBox(getWorld(), new BlockPos(x, y, z), getWorld().getBlockState(new BlockPos(x, y, z))) != null)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getBlock(double offset)
    {
        return getBlock(getPlayer().getEntityBoundingBox().offset(0.0D, offset, 0.0D));
    }


    public enum Mode {
        MEMEZ,
        ONGROUND,
        NCP,
    }

}
