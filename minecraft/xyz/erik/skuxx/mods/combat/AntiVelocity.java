package xyz.erik.skuxx.mods.combat;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

public class AntiVelocity
extends Mod{


    public AntiVelocity() {
        setCategory(Category.COMBAT);
    }

    @EventTarget
    private void onPacketReceive(EventPacket event)
    {
        if (event.type != EventPacket.Type.TAKE || getCancelTicks() > 0) {
            return;
        }
        if ((event.getPacket() instanceof S12PacketEntityVelocity))
        {
            S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)event.packet;
            if (this.minecraft.theWorld.getEntityByID(s12PacketEntityVelocity.func_149412_c()) == getPlayer())
            {

                setRunning(3);
                event.setCancelled(true);
            }
        }
        else if ((event.packet instanceof S27PacketExplosion))
        {
            S27PacketExplosion explo = (S27PacketExplosion)event.packet;
            explo.field_149152_f = 0.0F;
            explo.field_149153_g = 0;

            explo.field_149159_h = 0.0F;

            setRunning(3);
        }
    }
}
