package xyz.erik.skuxx.mods.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

public class Criticals
extends Mod
{

    public static boolean cancel;
    public Criticals() {
        super("Criticals",new String[]{"crit"});
        setCategory(Category.COMBAT);

    }

    @EventTarget
    private void sendPacketEvent(EventPacket event) {
        if (event.type == EventPacket.Type.SEND) {
            if (event.packet instanceof C02PacketUseEntity) {
                C02PacketUseEntity co2 = (C02PacketUseEntity)event.packet;
                Entity entity = co2.getEntityFromWorld(getWorld());
                if (co2.getAction() == C02PacketUseEntity.Action.ATTACK && ((EntityLivingBase)entity).hurtTime < 5) {
                    if (cancel) {
                        cancel = false;
                        return;
                    }
                    crit();
                }
            }
        }
    }


    void crit() {
        setRunning(3);
        if (!this.minecraft.thePlayer.isCollidedVertically) {
            return;
        }
        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.05, this.minecraft.thePlayer.posZ, false));
        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));
        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.012511D, this.minecraft.thePlayer.posZ, false));
        this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));


    }



    public static void setCancel(boolean cancel) {
        Criticals.cancel = cancel;
    }
}
