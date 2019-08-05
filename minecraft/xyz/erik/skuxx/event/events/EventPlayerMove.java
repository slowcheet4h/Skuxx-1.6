package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class EventPlayerMove extends Event{

    private State state;
    private final double oldX;
    private final double oldY;
    private final double oldZ;
    private double x;
    private double y;
    private double z;

    public EventPlayerMove(final double x, final double y, final double z) {
        this.oldX = x;
        this.x = x;
        this.oldY = y;
        this.y = y;
        this.oldZ = z;
        this.z = z;
    }

    public State getState() {
        return this.state;
    }

    public void multiplyX(final double x) {
        this.x *= x;
    }

    public void multiplyY(final double y) {
        this.y *= y;
    }

    public void multiplyZ(final double z) {
        this.z *= z;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public double getOldX() {
        return this.oldX;
    }

    public double getOldY() {
        return this.oldY;
    }

    public double getOldZ() {
        return this.oldZ;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public enum State
    {
        PRE("PRE", 0),
        POST("POST", 1);

        private State(final String s, final int n) {
        }
    }
}
