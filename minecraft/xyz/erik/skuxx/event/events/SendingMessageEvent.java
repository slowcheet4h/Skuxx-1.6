package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class SendingMessageEvent
extends Event
{
    private String message;

    public SendingMessageEvent(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
