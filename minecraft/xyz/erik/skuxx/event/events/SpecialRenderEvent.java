package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class SpecialRenderEvent
extends Event{

    private float partialTicks;

    public SpecialRenderEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return this.partialTicks;
    }
}
