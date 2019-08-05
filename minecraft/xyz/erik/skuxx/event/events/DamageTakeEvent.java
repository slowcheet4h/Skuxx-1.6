package xyz.erik.skuxx.event.events;

import xyz.erik.skuxx.event.Event;

public class DamageTakeEvent
extends Event{


    float damage;
    public DamageTakeEvent(float damage) {
        this.damage = damage;
    }


    public float getDamage() {
        return damage;
    }




}



