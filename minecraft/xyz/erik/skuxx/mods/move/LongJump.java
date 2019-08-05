package xyz.erik.skuxx.mods.move;

import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class LongJump
extends Mod{

        private int airTicks;
        private int groundTicks;

        public LongJump(){
            setCategory(Category.MOVE);
        }
        @EventTarget
    public void onEvent(EventMotion event)
    {
        if (event.getMotion() == Event.Motion.BEFORE)
        {

            boolean moving = (getMinecraft().gameSettings.keyBindForward.getIsKeyPressed()) || (getMinecraft().gameSettings.keyBindBack.getIsKeyPressed());
            if (!moving) {
                return;
            }
            double forward = getPlayer().movementInput.moveForward;
            float yaw = getPlayer().rotationYaw;
            if (forward != 0.0D)
            {
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }
            else {
                forward = 0.0D;
            }
            float[] motion = { 0.4206065F, 0.4179245F, 0.41525924F, 0.41261F, 0.409978F, 0.407361F, 0.404761F, 0.402178F, 0.399611F, 0.39706F, 0.394525F, 0.392F, 0.3894F, 0.38644F, 0.383655F, 0.381105F, 0.37867F, 0.37625F, 0.37384F, 0.37145F, 0.369F, 0.3666F, 0.3642F, 0.3618F, 0.35945F, 0.357F, 0.354F, 0.351F, 0.348F, 0.345F, 0.342F, 0.339F, 0.336F, 0.333F, 0.33F, 0.327F, 0.324F, 0.321F, 0.318F, 0.315F, 0.312F, 0.309F, 0.307F, 0.305F, 0.303F, 0.3F, 0.297F, 0.295F, 0.293F, 0.291F, 0.289F, 0.287F, 0.285F, 0.283F, 0.281F, 0.279F, 0.277F, 0.275F, 0.273F, 0.271F, 0.269F, 0.267F, 0.265F, 0.263F, 0.261F, 0.259F, 0.257F, 0.255F, 0.253F, 0.251F, 0.249F, 0.247F, 0.245F, 0.243F, 0.241F, 0.239F, 0.237F };
            float[] glide = { 0.3425F, 0.5445F, 0.65425F, 0.685F, 0.675F, 0.2F, 0.895F, 0.719F, 0.76F };
            if ((!getPlayer().isCollidedVertically) && (!getPlayer().onGround))
            {
                this.airTicks += 1;
                this.groundTicks = -5;
                if (!getPlayer().isCollidedVertically)
                {
                    if ((this.airTicks - 6 >= 0) && (this.airTicks - 6 < glide.length)) {
                        getPlayer().motionY *= glide[(this.airTicks - 6)];
                    }
                    if ((getPlayer().motionY < -0.2D) && (getPlayer().motionY > -0.24D)) {
                        getPlayer().motionY *= 0.7D;
                    } else if ((getPlayer().motionY < -0.25D) && (getPlayer().motionY > -0.32D)) {
                        getPlayer().motionY *= 0.8D;
                    } else if ((getPlayer().motionY < -0.35D) && (getPlayer().motionY > -0.8D)) {
                        getPlayer().motionY *= 0.98D;
                    }
                }
                if ((this.airTicks - 1 >= 0) && (this.airTicks - 1 < motion.length))
                {
                    getPlayer().motionX = (forward * motion[(this.airTicks - 1)] * 3.0D * Math.cos(Math.toRadians(yaw + 90.0F)));
                    getPlayer().motionZ = (forward * motion[(this.airTicks - 1)] * 3.0D * Math.sin(Math.toRadians(yaw + 90.0F)));
                }
                else
                {
                    getPlayer().motionX = 0.0D;
                    getPlayer().motionZ = 0.0D;
                }
            }
            else
            {
                this.airTicks = 0;
                this.groundTicks += 1;
                if (this.groundTicks <= 2)
                {
                    getPlayer().motionX = (forward * 0.009999999776482582D * Math.cos(Math.toRadians(yaw + 90.0F)));
                    getPlayer().motionZ = (forward * 0.009999999776482582D * Math.sin(Math.toRadians(yaw + 90.0F)));
                }
                else if (this.groundTicks > 2)
                {
                    getPlayer().motionX = (forward * 0.30000001192092896D * Math.cos(Math.toRadians(yaw + 90.0F)));
                    getPlayer().motionZ = (forward * 0.30000001192092896D * Math.sin(Math.toRadians(yaw + 90.0F)));
                    getPlayer().motionY = 0.42399999499320984D;
                }
            }
        }
    }

    public void onEnabled()
    {
        this.groundTicks = -5;
        super.onEnabled();
    }

}
