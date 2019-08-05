package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class EventPush
extends Event{

    private boolean push;
    public EventPush(boolean push) {
        this.push = push;
    }

    public boolean isPush() {
        return push;
    }
}
