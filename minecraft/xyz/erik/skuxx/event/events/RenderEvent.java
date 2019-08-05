package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class RenderEvent
extends Event
{


    private float partialTicks;

    public RenderEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
