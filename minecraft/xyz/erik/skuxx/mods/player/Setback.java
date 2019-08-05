package xyz.erik.skuxx.mods.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

public class Setback
extends Mod{

    private Bool damage = new Bool("Damage",false);
    boolean isDamaged;
    public Setback() {
        addSet(damage);
        setCategory(Category.PLAYER);
    }

    public void onEnabled() {
        isDamaged = false;
        if (getPlayer().MovementInput() && !isDamaged) {
            if (damage.isState() && getPlayer().onGround) {
                Helper.damagePlayer();
                isDamaged = true;
            }
        }
        super.onEnabled();
    }

    @EventTarget
    private void onPacketSend(EventPacket e) {
        if (e.getType() != EventPacket.Type.SEND) return;
        if ((e.getPacket() instanceof C03PacketPlayer)) {
            final C03PacketPlayer player = (C03PacketPlayer)e.getPacket();

            player.setY(player.getPositionY() + 0.068D);
            if (player.getRotating())
            {
                player.setPitch(player.getPitch() - Float.MIN_VALUE);
            }

        }
    }



    public void onDisabled() {
        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX,getPlayer().posY + 1,getPlayer().posZ,false));
        super.onDisabled();
    }
}
