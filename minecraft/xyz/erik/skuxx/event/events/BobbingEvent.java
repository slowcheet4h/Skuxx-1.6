package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class BobbingEvent
extends Event{

    private boolean bobbing;


    public BobbingEvent(boolean bobbing) {
        this.bobbing = bobbing;
    }

    public void setBobbing(boolean bobbing) {
        this.bobbing = bobbing;
    }

    public boolean isBobbing() {
        return bobbing;
    }
}
