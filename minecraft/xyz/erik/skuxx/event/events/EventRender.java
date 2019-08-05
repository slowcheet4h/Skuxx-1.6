package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class EventRender
extends Event
{

    private int partical;
    private int width,height;
    public EventRender(int width, int height,int partical) {
        this.partical = partical;
        this.height = height;
        this.width = width;
    }


    public int getPartical() {
        return partical;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
