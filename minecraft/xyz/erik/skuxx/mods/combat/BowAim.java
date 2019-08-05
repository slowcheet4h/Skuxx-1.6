package xyz.erik.skuxx.mods.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.friend.FriendManager;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BowAim
extends Mod{


    public Bool mobs = new Bool("Mobs",false);

    public BowAim() {
        setCategory(Category.COMBAT);
        addSet(mobs);
    }
    private boolean sendNext;
    private EntityLivingBase target;
    float yaw,pitch;




    @EventTarget
    private void update(EventMotion e) {
        if (e.getMotion() == Event.Motion.BEFORE) {

            if (getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemBow) {

                this.target = getTarget();
                if (this.target != null) {

setRunning(3);

                    if (sendNext) {
                        updateRotations();
                        e.setRotationYaw(yaw);
                        e.setRotationPitch(pitch);

                    }
                }


            }
        } else if (e.getMotion() == Event.Motion.AFTER) {
            if (target == null) {
                sendNext = false;
                return;
            }
            if (getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemBow) {
                if (sendNext) {

                    if (Skuxx.getInstance().getModManager().getMod("FastUse").getState()||Skuxx.getInstance().getModManager().getMod("ShootBow").getState()) {
                        updateRotations();
                        getPlayer().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(yaw,pitch,getPlayer().onGround));
                    }


                    getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
            }
        }
    }

    @EventTarget
    private void sendPacket(EventPacket e) {

        if (e.getType() == EventPacket.Type.SEND) {
            if (e.getPacket() instanceof C07PacketPlayerDigging) {
                if (getPlayer().getHeldItem() == null || getPlayer().getHeldItem().getItem() == null || !(getPlayer().getHeldItem().getItem() instanceof ItemBow)) {
                    return;
                }
                C07PacketPlayerDigging digging = (C07PacketPlayerDigging)e.getPacket();
                if (digging.func_180762_c() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                    if (sendNext && target != null) {
                        sendNext = false;
                    } else if (target != null){
                        sendNext = true;
                        e.setCancelled(true);
                    }
                }
            }




        }
    }


    public void onEnabled() {
        this.sendNext = false;
        super.onEnabled();
    }

    public double[] getAcceltionPosition() {
         if (this.target.lastTickPosX != this.target.posX || this.target.lastTickPosZ != this.target.posZ)
         {
             double x = this.target.posX + (this.target.posX - this.target.lastTickPosX) * 7.1 - getPlayer().getDistanceToEntity(this.target) / 20;
            double y = this.target.posY +  + (getPlayer().getDistanceToEntity(this.target) * 0.14 + (getPlayer().getItemInUseDuration() >= 14 ? 0 : getPlayer().getItemInUseDuration() * 0.07));
            double z = this.target.posZ + (this.target.posZ - this.target.lastTickPosZ) * 7.1 - getPlayer().getDistanceToEntity(this.target) / 20;
            return new double[]{x,y,z};
        } else {
            double y = this.target.posY +  + (getPlayer().getDistanceToEntity(this.target) * 0.14 + (getPlayer().getItemInUseDuration() >= 14 ? 0 : getPlayer().getItemInUseDuration() * 0.07));
            return new double[]{this.target.posX,y,this.target.posZ};
        }
    }

    public EntityLivingBase getTarget()
    {
        List<EntityLivingBase> targets = new ArrayList<>();
        for (Object o : getWorld().loadedEntityList) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase)o;
                if (entity instanceof EntityMob && !mobs.isState()) {
                    continue;
                }
                if (entity instanceof EntityPlayerSP) {
                    continue;
                }
                if (!getPlayer().canEntityBeSeen(entity))continue;

                if (entity instanceof EntityPlayer && Skuxx.getInstance().getFriendManager().isFriend(entity.getName())) continue;
                if (!entity.isEntityAlive()) continue;
                if (entity.getDistanceToEntity(getPlayer()) > 75)  continue;
                targets.add(entity);
            }
        }

        if (targets.size() == 0) return null;
        Collections.sort(targets, new Comparator<EntityLivingBase>() {
            @Override
            public int compare(EntityLivingBase o1, EntityLivingBase o2) {
                int yaw1 = (int)Helper.getDistanceBetweenAngles((float)Helper.getRotationsAtLocation(Location.CHEST,o1)[0],(float)getPlayer().rotationYaw);
                int yaw2 = (int)Helper.getDistanceBetweenAngles((float)Helper.getRotationsAtLocation(Location.CHEST,o2)[0],(float)getPlayer().rotationYaw);

                return yaw1 - yaw2;
            }
        });
        return targets.get(0);
    }

    public void updateRotations() {
        EntitySnowball ent = new EntitySnowball(getWorld());
        ent.setPosition(getAcceltionPosition());
        float[] rotations = Helper.getRotationsAtLocation(Location.HEAD,ent);

        this.yaw = rotations[0];
        this.pitch = rotations[1];
    }


}
