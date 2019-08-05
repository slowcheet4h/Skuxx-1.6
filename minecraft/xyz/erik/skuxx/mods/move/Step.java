package xyz.erik.skuxx.mods.move;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.StepEvent;
import xyz.erik.skuxx.mods.Category;

import java.util.Random;

public class Step
extends Mod
{

    public Step() {

        setCategory(Category.MOVE);
        addSet(birbucuk);
    }

    private int fix;
    private ErikTimer erikTimer = new ErikTimer();
    private double oldY;
    private Bool birbucuk = new Bool("1.5",true);
    @EventTarget
    private void onStep(StepEvent event) {
        if (!isMoving() || (!getPlayer().onGround || getPlayer().isOnLiquid()) || getPlayer().isOnLiquid() || getPlayer().isInLiquid() || getPlayer().isOnLadder() || !erikTimer.delay(250)) {
            return;
        }
        switch (event.getTime()) {
            case BEFORE:
                if (this.fix == 0) {
                     this.oldY = getPlayer().posY;
                    event.setHeight(this.canStep() ?birbucuk.isState() ?  2 : 1 : 0.5F);


                }
                break;
            case AFTER:

                double offset = Step.this.minecraft.thePlayer.getEntityBoundingBox().minY - Step.this.oldY;

                if (offset > 0.6D && offset <= 1&&this.fix == 0 && this.canStep()) {
                    if(canStep() && offset == 0.75) {
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.4099999964237213D, getPlayer().posZ, true));
                    }
                    else if (offset != 0.9375 && offset != 0.875)
                    {
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.4099999964237213D, getPlayer().posZ, true));
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + offset - 0.25, getPlayer().posZ, true));
                    } else {
                        if (offset == 0.9375) {
                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.4099999964237213D, getPlayer().posZ, true));
                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75, getPlayer().posZ, true));
                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1, getPlayer().posZ, true));
                        } else if (offset == 0.875) {
                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.4099999964237213D, getPlayer().posZ, true));
                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75, getPlayer().posZ, true));

                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1, getPlayer().posZ, true));

                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16, getPlayer().posZ, true));

                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.24, getPlayer().posZ, true));

                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16, getPlayer().posZ, true));

                            getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1, getPlayer().posZ, true));

                            getPlayer().motionX /= 2;
                            getPlayer().motionZ /= 2;
                        }
                    }

                    erikTimer.reset();
                    setRunning(2);
                    this.fix = 2;
                }
                if((fix == 0 && canStep() && offset == 1.125)) {

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));


                    setRunning(1);
                    erikTimer.reset();

                    fix = 2;
                    return;
                } else if(fix == 0 && canStep() && offset == 1.25) {
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16D, getPlayer().posZ, getPlayer().onGround));


                    fix = 1;
                    erikTimer.reset();
                    return;
                }else if(fix == 0 && canStep() && offset == 1.375) {
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.25D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.3D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));
                    setRunning(1);
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.37D, getPlayer().posZ, getPlayer().onGround));


                    fix = 2;
                    erikTimer.reset();
                    return;
                } else if(fix == 0 && canStep() && offset == 1.75) {
                    double[] array = new double[10];
                    array[0] = 0.4D;
                    array[1] = 0.72D;
                    array[2] = 0.5D;
                    array[3] = 0.41D;
                    array[4] = 0.83D;
                    array[5] = 1.16D;
                    array[6] = 1.41D;
                    array[7] = 1.57D;
                    array[8] = 1.58D;
                    array[9] = 1.42D;
                    for(double off : array) {
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX,getPlayer().posY + off,getPlayer().posZ,true));
                    }
                 erikTimer.reset();
                    fix = 3;
                    return;
                }else if(fix == 0 && canStep() && offset == 1.875) {
                    double[] array = new double[10];
                    array[0] = 0.4D;
                    array[1] = 0.72D;
                    array[2] = 0.5D;
                    array[3] = 0.41D;
                    array[4] = 0.83D;
                    array[5] = 1.16D;
                    array[6] = 1.41D;
                    array[7] = 1.57D;
                    array[8] = 1.58D;
                    array[9] = 1.42D;
                    for(double off : array) {
                        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX,getPlayer().posY + off,getPlayer().posZ,true));
                    }
                    erikTimer.reset();
                    fix = 3;
                    return;
                }

                if(fix == 0&& canStep() && offset == 1.9375) {
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.25D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.3D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));
                    setRunning(1);
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.37D, getPlayer().posZ, getPlayer().onGround));

                    erikTimer.reset();
                    return;

                }
                if ((this.fix == 0) && (canStep()) && (offset < 2.0D) && (offset > 1.0D) && offset != 2&& birbucuk.isState()) {


                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.25D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));

                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.3D, getPlayer().posZ, getPlayer().onGround));
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));
                    setRunning(1);
                    getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.4D, getPlayer().posZ, getPlayer().onGround));
                    this.fix = 1;
                    getMinecraft().getTimer().timerSpeed = 0.3F;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Thread.sleep(300);
                                getMinecraft().getTimer().timerSpeed = 1;
                            } catch (InterruptedException e) {
                            }
                        }
                    }).start();

                }
        }
    }

    @EventTarget
    private void Motion(EventMotion eventMotion) {
        if (eventMotion.getMotion() == EventMotion.Motion.BEFORE) {
            if (this.fix > 0) {
                eventMotion.setCancelled(true);
                fix--;
            }
            }
    }

    private boolean canStep()
    {
        return (!getPlayer().isOnLiquid()) && (!getPlayer().isInLiquid()) && (this.minecraft.thePlayer.onGround) && (!this.minecraft.gameSettings.keyBindJump.getIsKeyPressed()) && (this.minecraft.thePlayer.isCollidedVertically) && (this.minecraft.thePlayer.isCollidedHorizontally);
    }
}
