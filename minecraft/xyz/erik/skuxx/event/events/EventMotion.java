package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class EventMotion
extends Event
{
    private double oldPositionY;
    private float rotationYaw, rotationPitch;
    public double y;
    private boolean ground;
    private boolean send;
    private Event.Motion motion;
    private boolean lockView;


    public void setLockView(boolean lockView) {
        this.lockView = lockView;
    }


    public boolean isLockView() {
        return lockView;
    }

    public EventMotion(float yaw, float pitch, double y, boolean ground) {
        this.motion = Motion.BEFORE;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.y = y;
        this.ground = ground;
    }

    public void setRotationYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }

    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public void setY(double y) {
        this.y = y;
    }


    public EventMotion() {
        this.motion = Motion.AFTER;
    }


    public double getOldPositionY() {
        return oldPositionY;
    }

    public Motion getMotion() {
        return motion;
    }

    public void setRotations(float yaw,float pitch) {
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
    }
    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public double getY() {
        return y;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }
}
