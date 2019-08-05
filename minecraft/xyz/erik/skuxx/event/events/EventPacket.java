package xyz.erik.skuxx.event.events;

import net.minecraft.network.Packet;
import xyz.erik.skuxx.event.Event;

public class EventPacket
extends Event
{
    public Packet packet;
    public Type type;

    public EventPacket(Type type, Packet packet)
    {
        this.type = type;
        this.packet = packet;
    }

    public Packet getPacket()
    {
        return this.packet;
    }

    public static enum Type
    {
        SEND,  TAKE;


    }

    public Type getType() {
        return type;
    }
}
