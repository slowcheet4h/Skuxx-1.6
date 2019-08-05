package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class SprintEvent
extends Event{

    public boolean sprint;
    public SprintEvent(boolean sprint) {
        this.sprint = sprint;
    }
}
