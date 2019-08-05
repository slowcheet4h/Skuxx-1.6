package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class Render3D
extends Event{

    public float partialTicks;

    public Render3D(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
