package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class StepEvent
extends Event
{

    private final Time time;
    private float height;

    public StepEvent(float height)
    {
        this.time = Time.BEFORE;
        this.height = height;
    }

    public StepEvent(Time time)
    {
        this.time = time;
    }

    public Time getTime()
    {
        return this.time;
    }

    public float getHeight()
    {
        return this.height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public static enum Time
    {
        BEFORE,  AFTER;

        private Time() {}
    }
}
