package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class RenderRiding
extends Event
{

    private boolean riding;
    public RenderRiding(boolean riding) {
        this.riding = riding;
    }

    public boolean getRiding() {
        return riding;
    }

    public void setRiding(boolean riding) {
        this.riding = riding;
    }
}
