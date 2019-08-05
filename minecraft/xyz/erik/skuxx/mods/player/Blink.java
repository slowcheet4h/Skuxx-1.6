package xyz.erik.skuxx.mods.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.RenderEvent;
import xyz.erik.skuxx.event.events.SpecialRenderEvent;
import xyz.erik.skuxx.mods.Category;

import javax.lang.model.element.ElementVisitor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Blink
extends Mod
{
    private EntityOtherPlayerMP playerMP;
    private List<Crumb> crumbs = new ArrayList<>();
    private List<Packet> packetList = new ArrayList<>();
    private Bool lag = new Bool("Lag",false);
    private Int lagginPackets = new Int("lagpackets",9,1,42);
    public Blink() {
        addSet(lag);
        addSet(lagginPackets);
        setCategory(Category.PLAYER);
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (event.getType() != EventPacket.Type.SEND) return;
       if (event.packet instanceof C03PacketPlayer) {

           final C03PacketPlayer co3 = (C03PacketPlayer)event.getPacket();
           if (!Blink.this.isRecorded(co3.x, co3.y, co3.z) && co3.x != 0 && co3.y != 0 && co3.z != 0) {
               Blink.this.crumbs.add(new Crumb(co3.x,co3.y,co3.z));
           }
           if (getPlayer().lastTickPosX == getPlayer().posX && getPlayer().lastTickPosY == getPlayer().posY && getPlayer().lastTickPosZ == getPlayer().posZ
                   && getPlayer().rotationYaw == getPlayer().getLastYaw() && getPlayer().getLastPitch() == getPlayer().rotationPitch) {     event.setCancelled(true);return;}
           setSuffix(this.packetList.size() + "");
            event.setCancelled(true);
            packetList.add(co3);
       } else if (event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C07PacketPlayerDigging
               || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C09PacketHeldItemChange
               || event.getPacket() instanceof C10PacketCreativeInventoryAction || event.getPacket() instanceof C11PacketEnchantItem
               || event.getPacket() instanceof C12PacketUpdateSign || event.getPacket() instanceof C13PacketPlayerAbilities || event.getPacket() instanceof C15PacketClientSettings
               || event.getPacket() instanceof C16PacketClientStatus || event.getPacket() instanceof C17PacketCustomPayload || event.getPacket() instanceof C0BPacketEntityAction ||
               event.getPacket() instanceof C0APacketAnimation || event.getPacket() instanceof C0CPacketInput){
           packetList.add(event.getPacket());
            event.setCancelled(true);
       }
       if(lag.isState()) {
           if (packetList.size() > lagginPackets.getValue()) {
               this.onDisabled();
               this.onEnabled();
           }
       }
    }

    private boolean isRecorded(final double x, final double y, final double z) {
        final Iterator<Crumb> iterator = this.crumbs.iterator();
        if (iterator.hasNext()) {
            final Crumb crumb = iterator.next();
            return crumb.getX() == x && crumb.getY() == y && crumb.getZ() == z;
        }
        return false;

    }


    public void onEnabled() {
        if (getPlayer() == null) {
            toggle();
            return;
        }
        this.crumbs.clear();
        playerMP = new EntityOtherPlayerMP(getWorld(),getPlayer().getGameProfile());
        playerMP.setPosition(getPlayer().posX,getPlayer().posY,getPlayer().posZ);
        playerMP.setSneaking(getPlayer().isSneaking());
        playerMP.setRotationYawHead(getPlayer().rotationYaw);
        playerMP.rotationPitch = getPlayer().rotationPitch;
        playerMP.rotationYaw = getPlayer().rotationYaw;
        getWorld().addEntityToWorld(133,playerMP);
        super.onEnabled();
    }

    @EventTarget
    private void onRender(SpecialRenderEvent event) {
        GlStateManager.pushMatrix();
        RenderUtil.enableGL3D();
        GL11.glColor4f(0.27f, 0.7f, 0.27f, 1.0f);
        GL11.glBegin(3);

            this.crumbs.forEach(crumb -> {
           final double x = crumb.getX() - Blink.this.minecraft.getRenderManager().renderPosX;
           final double y = crumb.getY() - Blink.this.minecraft.thePlayer.height + 3.0 - Blink.this.minecraft.getRenderManager().renderPosY;
           final double z = crumb.getZ() - Blink.this.minecraft.getRenderManager().renderPosZ;
            GL11.glVertex3d(x, y, z);
            return;
        });
        GL11.glEnd();
        RenderUtil.disableGL3D();
        GlStateManager.popMatrix();
    }

    public void onDisabled() {
        if (getPlayer() == null) {
            super.onDisabled();
            return;
        }
        super.onDisabled();

        setSuffix("0");
        getWorld().removeEntityFromWorld(133);
         this.crumbs.clear();
        for(Packet p : packetList) {
            getPlayer().sendPacket(p);
        }
        packetList.clear();

    }

    private class Crumb
    {
        private final double x;
        private final double y;
        private final double z;

        public Crumb(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}
