package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class SafeWalkEvent
extends Event{

    boolean walk = false;

    public void setSafeWalk(boolean walk)
    {
        this.walk = walk;
    }

    public boolean getSafeWalk()
    {
        return this.walk;
    }
}
