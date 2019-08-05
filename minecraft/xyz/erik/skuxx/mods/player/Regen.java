package xyz.erik.skuxx.mods.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class Regen
extends Mod
{
    private Double health = new Double("Health",6,1,10);
    private Int packets = new Int("Packets",50,2,500);
    public Regen() {
        addSet(health);
        addSet(packets);
        setCategory(Category.PLAYER);
    }

    @EventTarget
    private void onMotion(EventMotion event) {
        if (event.getMotion() == Event.Motion.BEFORE) {
            if (getPlayer().getHealth() < health.getValue() * 2) {
                 for(int i = packets.getValue(); i > 0; i--) {
                     getPlayer().sendPacket(new C03PacketPlayer(getPlayer().onGround));
                 }
            }
        }
    }

}
