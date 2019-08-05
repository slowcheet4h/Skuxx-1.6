package xyz.erik.skuxx.mods.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;

public class Damage
extends Mod{

    public void onEnabled() {
        super.onEnabled();
    }

    @EventTarget
    private void onUpdate(EventMotion e) {
        e.setGround(true);
        if (getPlayer().onGround) {
            getPlayer().motionY = 0.4;
            double baseSpeed = 0.2873D;
            if (getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
            {
                int amplifier = getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
            }
        } else {
            getPlayer().motionY = -0.5;
        }
    }


}
