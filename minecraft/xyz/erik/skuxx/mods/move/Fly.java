package xyz.erik.skuxx.mods.move;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.events.BobbingEvent;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.RenderRiding;
import xyz.erik.skuxx.mods.Category;

public class Fly
extends Mod{

    private ErikTimer timer = new ErikTimer();
    private int meme;
    private double lastY;
    private Bool erik = new Bool("Erik",false);
    private Bool glide = new Bool("Glide",false);
    private Bool damage = new Bool("Damage",false);
    private Bool hypixel = new Bool("Hypixel",false);
    public Fly() {
        addSet(hypixel);
        addSet(damage);
        addSet(glide);
        addSet(erik);
        setCategory(Category.MOVE);
    }

    public void onEnabled() {
        if (damage.isState() && getPlayer().onGround) {
            Helper.damagePlayer();
        }
        Skuxx.getInstance().getSkuxxPlayer().setRiding(true);
        Skuxx.getInstance().updateSkuxxPlayer();
        super.onEnabled();
    }

    @EventTarget
    private void onMotion(EventMotion eventMotion) {
        if (erik.isState()) {
            setSuffix("Erik");
            if (eventMotion.getMotion() == Event.Motion.BEFORE) {
                //pink another brick in the wall
                eventMotion.setCancelled(getPlayer().ticksExisted % 2 == 0);

            }
        }
        else if (hypixel.isState()) {
            getPlayer().distanceWalkedModified = 1.1F;
            getPlayer().motionY = 0.0D;
            meme += 1;
                if (meme == 1) {
                    getPlayer().setPosition(getPlayer().posX, getPlayer().posY + 1.0E-12D, getPlayer().posZ);
                } else if (meme == 2) {
                    getPlayer().setPosition(getPlayer().posX, getPlayer().posY - 1.0E-12D, getPlayer().posZ);
                } else if(meme == 3){
                    getPlayer().setPosition(getPlayer().posX, getPlayer().posY - 1.0E-12D, getPlayer().posZ);
                    meme = 0;
                }

            System.out.println(meme);
            getPlayer().cameraYaw = 0.18181817F;
        } else if(glide.isState()) {
            getPlayer().motionY = -0.03D;
        } else {
            getPlayer().motionY = -0.0001;
            setSuffix("Nocheat");
        }

        if (!getPlayer().onGround) {
            if (getMinecraft().gameSettings.keyBindSneak.pressed) {
                getPlayer().motionY -= 0.23;
            }

        }
        if (getMinecraft().gameSettings.keyBindJump.pressed) {
            getPlayer().motionY += 0.22;
        }
    }

    public void onDisabled() {

        Skuxx.getInstance().getSkuxxPlayer().setRiding(false);
        Skuxx.getInstance().updateSkuxxPlayer();
        super.onDisabled();
    }

    private float height;
    private int uptime = 0;
    @EventTarget
    private void packet(EventPacket eventPacket) {
        if (eventPacket.getType() == EventPacket.Type.SEND) {

            if (glide.isState()) {
                setSuffix("Glide");
                if (eventPacket.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer player = (C03PacketPlayer)eventPacket.getPacket();
                    if (player.getPositionY() > this.lastY && !getPlayer().onGround) {
                        uptime++;
                    }
                    if (getPlayer().onGround) uptime = 0;

                    this.lastY = player.getPositionY();
                    if (timer.delay(1000L)) {
                        player.x = getMinecraft().thePlayer.posX;
                        player.y = (getMinecraft().thePlayer.posY + this.height);
                        player.field_149474_g = false;
                        timer.reset();
                    }
                    }
                }



        } else {
            if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
                getPlayer().jump();
            }
        }
    }


    @EventTarget
    private void onBob(BobbingEvent event) {
        if (!getMinecraft().gameSettings.keyBindSneak.pressed && !hypixel.isState())
        event.setBobbing(true);
    }

    @EventTarget
    private void RenderRiding(RenderRiding renderRiding) {
            renderRiding.setRiding(true);

    }

}
