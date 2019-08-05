package xyz.erik.skuxx.mods.player;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.play.client.C02PacketUseEntity;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventAttack;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class VehicleControl
extends Mod
{

    public VehicleControl() {

        setCategory(Category.PLAYER);
    }

    @EventTarget
    private void onAttack(EventAttack eventAttack) {
        if (eventAttack.getEntity() instanceof EntityMinecart) {

            for(int i = 0; i < 5; i++) {
                getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(eventAttack.getEntity(),C02PacketUseEntity.Action.ATTACK));
            }
        }
    }

    @EventTarget
    private void onMotion(EventMotion eventMotion) {
        if (eventMotion.getMotion() == Event.Motion.BEFORE) {

            if(getPlayer().isRiding() && !getPlayer().isRidingHorse()) {
                EntityMinecart entityMinecart = getClosestCart();
                if (entityMinecart != null) {
                    entityMinecart.mountEntity(getPlayer());


                }
            }


        }
    }


    public EntityMinecart getClosestCart() {
        EntityMinecart last = null;
        double dist = 66;
        for (Object o :
             this.minecraft.theWorld.loadedEntityList) {
            if(o instanceof EntityMinecart) {
                EntityMinecart entityMinecart = (EntityMinecart)o;
                if(entityMinecart.getDistanceToEntity(getPlayer()) < dist) {
                    dist = entityMinecart.getDistanceToEntity(getPlayer());
                    last = entityMinecart;
                }
            }
        }
        return last;
    }
}
