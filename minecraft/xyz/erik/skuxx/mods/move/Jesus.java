package xyz.erik.skuxx.mods.move;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.Sys;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.*;
import xyz.erik.skuxx.mods.Category;

import java.util.Random;

public class Jesus
extends Mod
{

    private ErikTimer timer = new ErikTimer();
    private double off = 0.02f;
    private Mode mode = Mode.SOLID;
    public Jesus() {
        super("Jesus",new String[]{"waterwalk","liquidwalk"});
        setCategory(Category.MOVE);
        this.modes = new String[]{"SOLID","DOLPHIN","BHOP"};
    }

    private int ticks;

    @EventTarget
    private void onJump(OnJump e) {
        if (getPlayer().isOnLiquid() && mode != Mode.SOLID) {
            e.setCancelled(true);
            timer.reset();
        }
    }
    private AxisAlignedBB axis;
    private ErikTimer inWaterTimer = new ErikTimer();
    @EventTarget
    private void onMotion(EventMotion eventMotion) {
        if (eventMotion.getMotion() == Event.Motion.BEFORE) {
            setSuffix(mode.suffix);
            if (mode == Mode.SOLID) {
                if (!getPlayer().movementInput.jump && getPlayer().isInLiquid() && !getPlayer().isSneaking() && !getPlayer().isCollidedVertically) {
                    getPlayer().motionY = 0.085D;
                }
            }

            if (mode == Mode.BHOP) {
                double forward = getPlayer().movementInput.moveForward;
                float yaw = getPlayer().rotationYaw;
                if (forward != 0.0D) {
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                } else {
                    forward = 0.0D;
                }
                if (getPlayer().isOnLiquid() && (inWaterTimer.delay(50) || inWaterTimer.getTime() == 0) && !getPlayer().isInWater() && !getPlayer().isSneaking()) {
                    getPlayer().motionY = 0.489;
                    eventMotion.setGround(getPlayer().ticksExisted % 2 == 0);

                }
                if (!getPlayer().isOnLiquid() && inWaterTimer.delay(50)) inWaterTimer.reset();

            }
            if (mode != Mode.SOLID && mode != Mode.BHOP) {
                if (getPlayer().onGround || getPlayer().isOnLadder()) {
                    this.wasWater = false;
                }

                if (getPlayer().motionY > 0.0D && this.wasWater) {
                    EntityPlayerSP arg1;
                    if (getPlayer().motionY <= 0.11D) {
                        arg1 = getPlayer();
                        arg1.motionY *= 1.2671D;
                    }

                    arg1 = getPlayer();
                    arg1.motionY += 0.05172D;
                }
                eventMotion.setGround(true);

                if (getPlayer().isInLiquid() && !getPlayer().isSneaking()) {
                    if (ticks < 3) {
                        getPlayer().motionY = 0.15D;
                        ++ticks;
                        this.wasWater = false;
                    } else {
                        getPlayer().motionY = 0.5D;
                        ticks = 0;
                        this.wasWater = true;
                    }
                }
            }
        }



    }
    private boolean wasWater;

    @EventTarget
    private void onBoundingBox(BlockBoundingBoxEvent event) {
        if (mode == Mode.SOLID) {
            if ((event.getState() != null) && ((event.getState().getBlock() instanceof BlockLiquid)) &&
                    (!getPlayer().isSneaking()) && (this.getPlayer().fallDistance <= 2.5F) && (event.getBlockPos().getY() < Jesus.this.minecraft.thePlayer.posY - this.off)) {
                boolean s = Helper.getBlock(getPlayer().posX, getPlayer().posY - 0.5f, getPlayer().posZ) instanceof BlockLiquid;
                if (!s) {
                    next = true;
                }
                setRunning(3);
                event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY() + 1, event.getBlockPos().getZ(), event.getBlockPos().getX() + 1.0f, event.getBlockPos().getY() + 1, event.getBlockPos().getZ() + 1.0F));

                axis = event.getBoundingBox();
            }

        } else if (mode == Mode.BHOP && !getPlayer().isSneaking() && event.getState().getBlock() instanceof BlockLiquid) {
            event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY() + 1 + - 0.3, event.getBlockPos().getZ(), event.getBlockPos().getX() + 1.0f, event.getBlockPos().getY() + 1 + -0.3, event.getBlockPos().getZ() + 1.0F));

            axis = event.getBoundingBox();
        } else if(mode == Mode.DOLPHIN && !getPlayer().isSneaking() && event.getState().getBlock() instanceof BlockLiquid) {
            event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY() + 1 - 0.1, event.getBlockPos().getZ(), event.getBlockPos().getX() + 1.0f, event.getBlockPos().getY() + 1 - 0.1, event.getBlockPos().getZ() + 1.0F));

            axis = event.getBoundingBox();
        }
    }


    @EventTarget
    private void render(EventRender e) {

    }


    private boolean next = true;

    @EventTarget
    private void packet(EventPacket eventPacket) {
        if (mode != Mode.SOLID) return;
        if (eventPacket.getPacket() instanceof C03PacketPlayer) { ;
            C03PacketPlayer player = (C03PacketPlayer)eventPacket.getPacket();
            if (getPlayer().isOnLiquid() && !getPlayer().isInLiquid() ) {

                if (getPlayer().movementInput.jump) return;
                boolean isInLava = Helper.getBlock(getPlayer().posX, getPlayer().posY - 0.5f, getPlayer().posZ) != Blocks.water;
                if (isInLava && !getPlayer().MovementInput()) {
                    eventPacket.setCancelled(true);
                    return;
                }
                if(isInLava) {
                    if (getPlayer().isSprinting()) {
                        this.getPlayer().sendQueue.addToSendQueue(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    player.field_149474_g = next;
                    this.next = (!next);
                    player.setY(player.getY() - (next ? -0.0001 : 0.0001    ));
                }
                if (!isInLava && getPlayer().ticksExisted % 2 == 0 && getPlayer().isBurning()) {
                    player.setY(player.getY() - 0.405);
                } else if (!isInLava){
                    player.setY(player.getY() - (getPlayer().ticksExisted % 2 == 0 ? 0.0001 : 0));
                }


            }


        }
    }

    private enum Mode
    {
        SOLID("Solid"),DOLPHIN("Dolphin"),BHOP("Bhop");
        private String suffix;
        Mode(String suffix) {
            this.suffix = suffix;
        }
    }


    public String setMode(String mode) {
        switch (mode.toLowerCase()) {
            case "solid":
                this.mode = Mode.SOLID;
                break;
            case "dolphin":
                this.mode = Mode.DOLPHIN;
                break;
            case "bhop":
                this.mode = Mode.BHOP;
                break;
        }
        return "Jesus mode changed to " + this.mode.suffix;


    }
}
