package xyz.erik.skuxx.mods.move;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class Sneakers
extends Mod{


    public Sneakers() {
        super("Sneakers",new String[]{"sneak","autosneak"});

        setCategory(Category.MOVE);
    }

    @EventTarget
    private void onMotion(EventMotion update) {
        if (update.getMotion() == Event.Motion.BEFORE) {
            if (getPlayer().MovementInput()) {
                getPlayer().sendQueue.addToSendQueue(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        } else if(update.getMotion() == Event.Motion.AFTER) {
            getPlayer().sendQueue.addToSendQueue(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.START_SNEAKING));
            setRunning(2);
        }
    }

    @Override
    public void onDisabled() {
        if (getPlayer() != null) {
            getPlayer().sendPacket(new C0BPacketEntityAction(getPlayer(),C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        super.onDisabled();
    }
}
