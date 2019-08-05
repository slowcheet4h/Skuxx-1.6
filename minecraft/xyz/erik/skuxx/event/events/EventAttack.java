package xyz.erik.skuxx.event.events;

import net.minecraft.entity.Entity;
import xyz.erik.skuxx.event.Event;

public class EventAttack
extends Event{

    private Entity entity;

    public EventAttack(Entity ent) {
        this.entity = ent;
    }
    public Entity getEntity() {
        return entity;
    }
}
