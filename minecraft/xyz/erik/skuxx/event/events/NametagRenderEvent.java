package xyz.erik.skuxx.event.events;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import xyz.erik.skuxx.event.Event;

public class NametagRenderEvent
extends Event{

    public EntityOtherPlayerMP entity;
    public String string;
    public double x;
    public double y;
    public double z;

    public NametagRenderEvent(EntityOtherPlayerMP entity, String string, double x, double y, double z)
    {
        this.entity = entity;
        this.string = string;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
