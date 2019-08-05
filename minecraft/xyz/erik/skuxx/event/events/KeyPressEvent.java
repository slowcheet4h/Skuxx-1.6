package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class KeyPressEvent
extends Event
{
    private int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
