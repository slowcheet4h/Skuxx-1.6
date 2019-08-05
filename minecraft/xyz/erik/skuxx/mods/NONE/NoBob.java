package xyz.erik.skuxx.mods.NONE;

import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;

public class NoBob
extends Mod{



    @EventTarget
    private void onUpdate(EventMotion eventMotion) {
        if (eventMotion.getMotion() == Event.Motion.BEFORE)
        {
            getPlayer().distanceWalkedModified = 0.0F;
        }
    }

}
