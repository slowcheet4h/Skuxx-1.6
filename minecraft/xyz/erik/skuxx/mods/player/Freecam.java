package xyz.erik.skuxx.mods.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.BlockBoundingBoxEvent;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.EventPush;
import xyz.erik.skuxx.mods.Category;

public class Freecam
extends Mod
{

    private ErikTimer timer = new ErikTimer();
    private Bool packet = new Bool("Packet",false);
    private EntityOtherPlayerMP entity;
    private boolean sneaking;

    public Freecam() {
        addSet(packet);

        setCategory(Category.PLAYER);
    }
    @EventTarget
    private void onBB(BlockBoundingBoxEvent e) {
       e.setCancelled(true);
    }


    public void onEnabled() {
        entity = new EntityOtherPlayerMP(getWorld(),getPlayer().getGameProfile());
        getWorld().addEntityToWorld(-55,entity);
        this.sneaking = getPlayer().isSneaking();
        entity.setSneaking(this.sneaking);
        entity.setPosition(getPlayer().posX,getPlayer().posY,getPlayer().posZ);
        entity.onGround = getPlayer().onGround;
        entity.rotationYaw = getPlayer().rotationYaw;
        entity.inventory = getPlayer().inventory;
        entity.llololol = getPlayer().isChild();
        entity.inventoryContainer = getPlayer().inventoryContainer;
        entity.rotationPitch = getPlayer().rotationPitch;
        getPlayer().setPositionAndUpdate(getPlayer().posX,getPlayer().posY + 1,getPlayer().posZ);

        getPlayer().capabilities.isFlying = true;
        super.onEnabled();
    }


    @EventTarget
    private void onPacketSend(EventPacket event) {


        if (event.getType() == EventPacket.Type.SEND) {
            if (event.getPacket() instanceof C0BPacketEntityAction) {
                C0BPacketEntityAction cob = (C0BPacketEntityAction)event.getPacket();
                if (sneaking && cob.func_180764_b() == C0BPacketEntityAction.Action.STOP_SNEAKING) {
                    event.setCancelled(true);
                }
            }
            if (event.getPacket() instanceof C03PacketPlayer) {
                entity.inventory.currentItem = getPlayer().inventory.currentItem;
                getPlayer().onGround = entity.onGround;
                C03PacketPlayer co3 = (C03PacketPlayer)event.getPacket();
                entity.hurtTime = getPlayer().hurtTime;
                entity.hurtResistantTime = getPlayer().hurtResistantTime;
                getPlayer().renderArmPitch += 400.0F;

                if (this.timer.delay(9000L) && (getPlayer().motionZ != 0 || getPlayer().motionX != 0 || getPlayer().motionY != 0)) {
                    getMinecraft().renderGlobal.loadRenderers();
                    this.timer.reset();
                }


                if (getMinecraft().gameSettings.keyBindUseItem.pressed) {
                    getPlayer().onGround = entity.onGround;
                    entity.rotationYaw = getPlayer().rotationYaw;
                    entity.rotationPitch = getPlayer().rotationPitch;
                    entity.rotationYawHead = getPlayer().rotationYaw;
                    co3.setYaw(entity.rotationYaw);

                    co3.setPitch(entity.rotationPitch);
                } else if(packet.isState()) {
                    event.packet = new C03PacketPlayer();
                } else{
                    event.setCancelled(true);
                }
                co3.setX(entity.posX);
                co3.setY(entity.posY);
                co3.setZ(entity.posZ);

            }

        }
    }

    public void onDisabled() {
        getPlayer().setPositionAndUpdate(entity.posX,entity.posY,entity.posZ);
        getPlayer().capabilities.isFlying = false;
        getPlayer().rotationYaw = entity.rotationYaw;
        getPlayer().rotationPitch = entity.rotationPitch;
        if (sneaking) {
            getPlayer().sendPacket(new C0BPacketEntityAction(getPlayer(),C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        getWorld().removeEntityFromWorld(-55);
        super.onDisabled();
    }

    @EventTarget
    private void onPush(EventPush e) {
        e.setCancelled(true);
    }
    private boolean isUsingItem() {
        return getPlayer().isUsingItem() && getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemBow;
    }
}
