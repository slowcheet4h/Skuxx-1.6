package xyz.erik.skuxx.mods.combat;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.friend.Friend;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.cfg.SkuxxConfig;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.MiddleClickEvent;
import xyz.erik.skuxx.event.events.OnJump;
import xyz.erik.skuxx.mods.Category;


import java.util.*;

public class Aura
        extends Mod {

    public Aura() {
        super("Aura", new String[]{"ka", "killaura"});
        modes = new String[]{"Switch", "Tick", "Hurttime", "AAC", "Multi", "Tick2", "Crow"};
        addSet(range);
        addSet(mobs);
        addSet(players);
        addSet(speed);
        setCategory(Category.COMBAT);
        addSet(fakeSwing);
        addSet(block);
        addSet(breaker);
        addSet(renderHead);
        addSet(maxTargets);
        addSet(swing);
        this.mod = this.mode.suffix;
        setSuffix(Aura.this.mode.suffix);
    }
    private Bool swing = new Bool("Swing",true);
    private int swappedSlot;
    private boolean reBlock;
    private Bool renderHead = new Bool("RenderHead", false);
    private Bool friend = new Bool("Friend", false);

    private double[] crits = {0.065, 0.0656, 0.066, 0.067, 0.068, 0.069, 0.07};
    private boolean nextJump;
    private boolean critAttackNext;
    private boolean attackNext;
    private Bool fakeSwing = new Bool("FakeSwing", false);
    private Bool lock = new Bool("Lock",false);
    private int index;
    private EntityLivingBase target;
    private boolean jump;
    private LinkedList<EntityLivingBase> deathNote = new LinkedList<>();
    public Mode mode = Mode.SWITCH;
    public Bool breaker = new Bool("Dura", false);
    public Bool mobs = new Bool("Mobs", true);
    public Bool players = new Bool("Players", true);
    public Int range = new Int("Range", 5, 1, 8);
    public Double speed = new Double("Speed", 8, 1, 20);
    public Bool block = new Bool("Block", true);
    public Int maxTargets = new Int("MaxTarget", 6, 1, 3);
    private ErikTimer changeTimer = new ErikTimer();

    private int aps;
    private boolean next;
    private int x;


    @EventTarget
    private void onJump(OnJump e) {
        if (!next && this.jump) {
            e.setCancelled(true);
            nextJump = true;
            this.jump = false;
        }

    }

    private ErikTimer hurtTimer = new ErikTimer();
    private boolean attacked;
    private int timeSinceAttacked;
    private int fuckGiven;
    private boolean memez;
    private boolean swapped;

    @EventTarget
    private void onEvent(EventMotion eventMotion) {

        if (getRunningTicks() < 0) {
            setRunning(getRunningTicks() + 2);
            return;
        }
        if (nextJump && !jump) {
            getPlayer().jump();
            nextJump = false;
        }
        switch (mode.ordinal()) {
            case 0:
                if (eventMotion.getMotion() == Event.Motion.BEFORE) {
                    for (int i = 0; i < this.deathNote.size(); i++) {
                        EntityLivingBase e = this.deathNote.get(i);
                        if (!isValid(e)) {
                            this.deathNote.remove(i);
                        }
                    }
                    if (this.deathNote.size() == 0) {
                        deathNote = getDeathNote();
                    }
                    if (deathNote.size() > 0) {
                        if ((!isValid(target)) || changeTimer.delay(getNextTargetDelay())) {
                            if (changeTimer.delay(getNextTargetDelay())) {
                                this.timeSinceAttacked = -5;
                                changeTimer.reset();
                            } else {
                                this.target = getNewTarget();
                            }
                        }
                        if (target != null) {
                            if (getPlayer().isUsingSword() && block.isState()) {
                                block();
                            }
                            float[] values;
                            values = Helper.getRotationsAtLocation(Location.HEAD, target);
                            aps++;
                            eventMotion.setRotationYaw(Helper.wrapAngleTo180(values[0]));
                            eventMotion.setRotationPitch(Helper.wrapAngleTo180(values[1]));
                            eventMotion.setLockView(lock.isState());
                            if (renderHead.isState()) {
                                getPlayer().rotationYawHead = eventMotion.getRotationYaw();
                            }
                            if (aps >= (20 / speed.getValue()) && Skuxx.getInstance().getModManager().getMod("Criticals").getState() && !(Skuxx.getInstance().getModManager().getMod("Step").isRunning())) {
                                jump = true;
                                if (getPlayer().onGround && next) {
                                    double crit = crits[x];
                                    eventMotion.setY(eventMotion.getY() + crit);
                                    eventMotion.setGround(false);
                                    eventMotion.setSend(true);
                                    x++;
                                    if(x >= crits.length) x = 0;

                                } else if (getPlayer().onGround) {
                                    eventMotion.setGround(false);
                                    eventMotion.setSend(true);
                                }
                                if (getPlayer().fallDistance > 0.0F && getPlayer().fallDistance < 0.69F) {
                                    eventMotion.setGround(true);
                                }
                                Skuxx.getInstance().getModManager().getMod("Criticals").setRunning(1);
                            }
                            next = (!next);
                        }

                    }
                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (deathNote.size() != 0 && target != null) {
                        if (aps >= (20 / speed.getValue()) && next) {
                            aps = 0;

                            if (getPlayer().isBlocking()) {
                                getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }

                            Criticals.setCancel(true);
                            attack(target);

                            if (timeSinceAttacked == -5) {
                                this.deathNote.remove(this.target);
                                this.timeSinceAttacked = 0;
                                index = 0;
                                if (this.deathNote.size() != 0) {
                                    Collections.sort(this.deathNote, new Comparator<EntityLivingBase>() {
                                        @Override
                                        public int compare(EntityLivingBase fruit2, EntityLivingBase fruit1) {
                                            return (int) Helper.getRotationsAtLocation(Location.FEET, fruit1)[0] - (int) Helper.getRotationsAtLocation(Location.FEET, fruit2)[0];
                                        }
                                    });
                                    this.target = getIndexTarget();
                                }
                            }
                            if (getPlayer().isBlocking()) {
                                getPlayer().sendPacket(new C08PacketPlayerBlockPlacement(this.getPlayer().getHeldItem()));
                            }
                        }
                    }
                }
                break;
            case 1:
                if (eventMotion.getMotion() == Event.Motion.BEFORE) {

                    this.deathNote = getDeathNote();
                    for (Object o : this.getWorld().loadedEntityList) {
                        if (o instanceof EntityLivingBase) {
                            EntityLivingBase e = (EntityLivingBase) o;
                            e.tick--;
                        }
                    }

                    if (this.deathNote.size() != 0) {

                        if ((this.changeTimer.delay(breaker.isState() ? 500 : getNextTargetDelay())) || (!isValid(this.target) && changeTimer.delay(breaker.isState() ? 500 : getNextTargetDelay()))) {
                            this.target = getNewTarget();
                            changeTimer.reset();
                        }

                        if (this.target != null) {
                            if (getPlayer().isBlocking() || (this.block.isState() && getPlayer().isUsingSword())) {
                                block();
                            }
                            float[] rotations = Helper.getRotationsAtLocation(Location.CHEST, this.target);
                            eventMotion.setRotationYaw(rotations[0]);
                            eventMotion.setRotationPitch(rotations[1]);
                            eventMotion.setLockView(lock.isState());


                        }
                    }


                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (this.deathNote.size() > 0 && this.target != null) {
                        if (hurtTimer.delay(509)) {
                            if (breaker.isState()) {
                                if (getPlayer().isBlocking()) {
                                    getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                                }


                                this.swappedSlot = getNUsed();
                                getPlayer().swap(this.swappedSlot, this.getPlayer().inventory.currentItem);
                                attack(this.target, false);
                                attack(this.target, true);
                                attack(this.target, false);
                                getPlayer().swap(this.swappedSlot, this.getPlayer().inventory.currentItem);
                                attack(this.target, false);
                                attack(this.target, true);
                            } else if (!breaker.isState()) {
                                Criticals.setCancel(true);
                                this.attack(this.target);
                                Criticals.setCancel(true);
                                this.attack(this.target);
                                Criticals.setCancel(true);
                                this.attack(this.target);
                            }

                            hurtTimer.reset();
                        }

                    } else {
                        if (this.swapped) {
                            getPlayer().swap(swappedSlot, this.getPlayer().inventory.currentItem);
                            this.swapped = false;
                        }
                    }

                }

                break;
            case 2:
                if (eventMotion.getMotion() == Event.Motion.BEFORE) {
                    this.deathNote = getDeathNote();
                    if (deathNote.size() != 0) {
                        this.deathNote.sort(new Comparator<EntityLivingBase>() {
                            @Override
                            public int compare(EntityLivingBase o1, EntityLivingBase o2) {
                                return o1.getHurt() - o2.getHurt();
                            }
                        });
                        if (!isValid(target) || changeTimer.delay(300)) {
                            this.target = getNewTarget();
                            changeTimer.reset();
                        }
                        if (isValid(target)) {
                            if (block.isState() && getPlayer().isUsingSword()) {
                                block();
                            }
                            float[] vals = Helper.getRotationsAtLocation(Location.FEET, Aura.this.target);
                            eventMotion.setRotationYaw(vals[0]);
                            eventMotion.setRotationPitch(vals[1]);
                            eventMotion.setLockView(lock.isState());

                            if (renderHead.isState()) {
                                getPlayer().rotationYawHead = eventMotion.getRotationYaw();
                            }

                        }
                    }
                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (isValid(target) && this.deathNote.size() != 0) {
                        if (target.hurttime()) {
                            if (getPlayer().isBlocking()) {
                                getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                        new BlockPos(0, 0, 0), EnumFacing.DOWN));
                            }
                            if (breaker.isState()) {
                                getPlayer().swap(9, getPlayer().inventory.currentItem);
                                this.reBlock = false;
                                this.attack(this.target, false);
                                this.attack(this.target, false);
                                this.attack(this.target, true);
                                getPlayer().swap(9, getPlayer().inventory.currentItem);

                                this.attack(this.target, false);
                                this.attack(this.target, true);

                            } else {
                                attack(target, false);
                                attack(target, false);
                                attack(target, true);
                            }

                            target.resetHurttime();
                        }
                    }

                }

            case 3:
                if (eventMotion.getMotion() == Event.Motion.BEFORE) {
                    if (isValid(aacTarget)) {
                        float[] rotations = Helper.getRotationsAtLocation(Location.CHEST, aacTarget);
                        int rn = new Random().nextInt(5);
                        if (getPlayer().ticksExisted % 2 == 0) {
                            rn = -rn;
                        }
                        eventMotion.setRotationYaw(rotations[0] + rn);
                        eventMotion.setRotationPitch(rotations[1]);
                        eventMotion.setLockView(lock.isState());
                        if (renderHead.isState()) {
                            getPlayer().rotationYawHead = eventMotion.getRotationYaw();
                        }

                    }

                    aps += 1;
                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (isValid(aacTarget)) {
                        if (aacTarget.isCanHurt(true)) {
                            if (getPlayer().isSprinting()) {
                                getPlayer().setSprinting(false);
                                getPlayer().sendPacket(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SPRINTING));
                            }
                            attack(aacTarget);
                            if (aps >= 20) aps = 0;
                        }

                    }
                }
                break;
            case 4:
                if (eventMotion.getMotion() == Event.Motion.BEFORE) {

                    this.deathNote = getMultiTargets();
                    if (this.deathNote.size() > 0) {
                        if (shouldBlock() && this.block.isState()) {
                            block();
                        }
                        float[] rotations = Helper.getAverageRotations(this.deathNote);
                        eventMotion.setRotationYaw(rotations[0]);
                        eventMotion.setRotationPitch(0);
                        aps++;
                        eventMotion.setLockView(lock.isState());
                        if (renderHead.isState()) {
                            getPlayer().rotationYawHead = eventMotion.getRotationYaw();
                        }

                        if (aps >= 11 && Skuxx.getInstance().getModManager().getMod("Criticals").getState() && !(Skuxx.getInstance().getModManager().getMod("Step").isRunning())) {
                            jump = true;
                            if (getPlayer().onGround && next) {
                                double crit = crits[new Random().nextInt(crits.length - 1)];
                                eventMotion.setY(eventMotion.getY() + crit);
                                eventMotion.setGround(false);
                                eventMotion.setSend(true);
                            } else if (getPlayer().onGround) {
                                eventMotion.setGround(false);
                                eventMotion.setSend(true);
                            }
                            if (getPlayer().fallDistance > 0.0F && getPlayer().fallDistance < 0.69F) {
                                eventMotion.setGround(true);
                            }
                            Skuxx.getInstance().getModManager().getMod("Criticals").setRunning(1);
                        }
                    }
                    next = (!next);
                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (this.deathNote.size() > 0 && next && aps >= 11) {

                        if (getPlayer().isBlocking()) {
                            getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        for (EntityLivingBase entity : this.deathNote) {
                            Criticals.setCancel(true);
                            attack(entity);
                        }
                        aps = 0;
                    }
                }
                break;


            case 5:
                final EventMotion e = eventMotion;

                if (e.getMotion() == Event.Motion.BEFORE) {
                    this.deathNote = getDeathNote();
                    this.fuckGiven -= 1;
                    if (this.deathNote.size() == 0) {
                        if (this.swapped) {
                            getPlayer().swap(this.swappedSlot, getPlayer().inventory.currentItem);
                            this.swapped = false;
                        }
                        this.target.tempHit = false;
                    } else {
                        this.target = this.deathNote.get(0);
                    }
                    if (this.target != null && this.deathNote.size() > 0) {
                        if (getPlayer().isBlocking() || (getPlayer().isUsingSword() && this.block.isState())) {
                            block();
                        }
                        float[] rotations = Helper.getRotationsAtLocation(Location.CHEST, this.target);
                        e.setRotationYaw(rotations[0]);
                        e.setRotationPitch(rotations[1]);
                        if (getPlayer().onGround && (this.fuckGiven <= 0)) {
                            jump = true;
                            if (critAttackNext) {
                                e.setY(e.getY() + 0.07);
                                e.setGround(false);
                                e.setSend(true);
                            } else {
                                e.setGround(false);
                                e.setSend(true);
                            }
                        }

                        critAttackNext = (!critAttackNext);

                        eventMotion.setY(e.getY());
                        eventMotion.setGround(e.isGround());
                        eventMotion.setSend(e.isSend());
                        eventMotion.setCancelled(e.isCancelled());
                    }


                } else if (e.getMotion() == Event.Motion.AFTER) {
                    if (this.deathNote.size() != 0 && this.target != null) {

                        if (critAttackNext && fakeSwing.isState()) {
                            getPlayer().swingItem();
                        }


                        if (fuckGiven <= 0 && critAttackNext == memez) {
                            if (getPlayer().isBlocking()) {
                                getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            }
                            if (breaker.isState()) {

                                System.out.println(this.critAttackNext);

                                if (this.critAttackNext) {
                                    this.swappedSlot = getNUsed();
                                    getPlayer().swap(this.swappedSlot, getPlayer().inventory.currentItem);

                                    Criticals.setCancel(true);
                                    this.attack(this.target, false);
                                    Criticals.setCancel(true);
                                    this.attack(this.target, true);

                                    getPlayer().swap(this.swappedSlot, getPlayer().inventory.currentItem);


                                    Criticals.setCancel(true);
                                    this.attack(this.target, false);
                                    Criticals.setCancel(true);
                                    this.attack(this.target, true);

                                    this.swapped = false;
                                    this.fuckGiven = 10;
                                } else {

                                    swapped = true;

                                }
                                this.memez = !this.critAttackNext;

                            } else {
                                if (this.critAttackNext) {
                                    Criticals.setCancel(true);
                                    this.attack(this.target);
                                    Criticals.setCancel(true);
                                    this.attack(this.target);

                                    Criticals.setCancel(true);
                                    this.attack(this.target);

                                    Criticals.setCancel(true);
                                    this.attack(this.target);
                                    this.fuckGiven = 10;
                                    this.critAttackNext = false;
                                } else {
                                    this.critAttackNext = true;
                                }
                            }

                        }
                    }
                }

                break;
            case 6:

                if (eventMotion.getMotion() == Event.Motion.BEFORE) {

                    this.deathNote = getDeathNote();
                    if (this.deathNote.size() > 0) {


                        if (!isValid(this.target) || changeTimer.delay(500)) {
                            this.target = getNewTarget();
                        }

                        if (!isValid(this.target)) return;
                        if (getPlayer().isUsingSword() && block.isState()) {
                            getPlayerController().sendUseItem(getPlayer(),getWorld(),getPlayer().getHeldItem());
                        }
                        float[] rotations = Helper.getRotationsAtLocation(Location.CHEST,this.target);
                        eventMotion.setRotations(rotations[0],rotations[1]);
                        eventMotion.setLockView(lock.isState());





                    } else {
                        if(swapped && swappedSlot != -1) {
                            getPlayer().swap(this.swappedSlot,getPlayer().inventory.currentItem);
                            swapped = false;
                        }
                    }
                } else if (eventMotion.getMotion() == Event.Motion.AFTER) {
                    if (this.deathNote.size() > 0) {

                        if (isValid(this.target)) {

                            if (hurtTimer.delay(263)) {

                                if (getPlayer().isBlocking()) {
                                    getPlayer().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,BlockPos.ORIGIN,EnumFacing.DOWN));
                                }
                                if (breaker.isState()) {
                                    getPlayer().swapfuckgoback(9,getPlayer().inventory.currentItem);
                                    this.attack(this.target,false);

                                    getPlayer().swapfuckgoback(9,getPlayer().inventory.currentItem);
                                    this.attack(this.target,true);
                                } else {
                                    this.attack(this.target, true);
                                }
                                hurtTimer.reset();
                                if (getPlayer().isBlocking()) {
                                    getPlayer().sendPacket(new C08PacketPlayerBlockPlacement(this.getPlayer().getHeldItem()));
                                }
                            }
                        }

                    }
                }
        }
    }


    private boolean next2tickAttack;


    private void block() {
        if (getPlayer().motionX != 0 || getPlayer().motionZ != 0) {
            getPlayer().setItemInUse(getPlayer().getHeldItem(),1336);
        } else {
            getPlayerController().sendUseItem(getPlayer(),getWorld(),getPlayer().getHeldItem());
        }
    }

    public EntityLivingBase getClosestTarget(EntityLivingBase entityLivingBase) {
        double dist = 2;
        EntityLivingBase closest = null;
        for(EntityLivingBase entity : deathNote) {
            if (entityLivingBase == entity || !isValid(entity)) continue;
            if(entityLivingBase.getDistanceToEntity(entity) < dist) {
                dist = entityLivingBase.getDistanceToEntity(entity);
                closest = entity;
            }
        }
        if (closest == null) {
            index++;
            return getIndexTarget();
        }
        return closest;
    }
    private void attack(EntityLivingBase ent, boolean crit)
    {
        if (swing.isState())
        getPlayer().swingItem();
        else
            getPlayer().sendPacket(new C0APacketAnimation());

        Criticals.setCancel(true);
        if (crit){

            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.05, this.minecraft.thePlayer.posZ, false));
            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));
            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.012511D, this.minecraft.thePlayer.posZ, false));
            this.minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, false));

        } else {
            this.getPlayer().sendPacket(new C03PacketPlayer(true));
        }
        getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(ent,C02PacketUseEntity.Action.ATTACK));

    }

    public List<EntityLivingBase> getTargetInSameRange() {
        List<EntityLivingBase>
                oEntitys = new ArrayList<>();
        final int _ANGLE_RANGE_ = 45;
        for(EntityLivingBase entityLivingBase : deathNote) {
            final float targetYaw = Helper.getRotationsAtLocation(Location.CHEST,entityLivingBase)[0];
            final float targetPitch = Helper.getRotationsAtLocation(Location.CHEST,entityLivingBase)[0];
            if(Helper.getDistanceBetweenAngles(targetYaw,(float)getPlayer().getLastYaw()) < _ANGLE_RANGE_
                    && Helper.getDistanceBetweenAngles(targetPitch,(float)getPlayer().getLastPitch()) < _ANGLE_RANGE_) {
                oEntitys.add(entityLivingBase);
            }
        }
        Collections.sort(oEntitys,new Comparator<EntityLivingBase>() {
            @Override
            public int compare(EntityLivingBase fruit2, EntityLivingBase fruit1) {
                return java.lang.Float.compare(Helper.getRotationsAtLocation(Location.FEET,fruit2)[0], Helper.getRotationsAtLocation(Location.FEET,fruit1)[0]);
            }
        });

        return oEntitys;

    }
    int tickX;


    public void attackLikeAngryCat(EntityLivingBase entity, int angryLevel,boolean swap) {
        boolean blocking = getPlayer().isBlocking();
        if (blocking) {
            getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN,EnumFacing.DOWN));
        }
        for(int i = 0; i < angryLevel ;i++) {

            if (swing.isState())
                getPlayer().swingItem();
            else
                getPlayer().sendPacket(new C0APacketAnimation());

            if(i % 2 == 0) {
                Criticals.setCancel(true);
                if(swap) {
                    getPlayer().swap(9,this.getPlayer().inventory.currentItem);
                }
            } else
            {
                Criticals.setCancel(false);
            }

            getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(entity  ,C02PacketUseEntity.Action.ATTACK));
        }
        if (blocking) {
            getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(getPlayer().getHeldItem()));
        }


    }


    private LinkedList<EntityLivingBase> getDeathNote() {
        LinkedList<EntityLivingBase> list = new LinkedList<>();
        for (Object e:
                getWorld().loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase)e;
                if (isValid(entityLivingBase)) {
                    list.add(entityLivingBase);
                }
            }
        }
        if (list.size() == 1) {
            return list;
        }
        Collections.sort(list, new Comparator<EntityLivingBase>() {

            @Override
            public int compare(EntityLivingBase fruit2, EntityLivingBase fruit1) {
                return (int) Helper.getRotationsAtLocation(Location.FEET, fruit1)[0] - (int) Helper.getRotationsAtLocation(Location.FEET, fruit2)[0];
            }

        });


        return list;
    }

    private EntityLivingBase getNextTarget() {
        int tempIndex = index;
        if (deathNote.size() >= index) {
            tempIndex = 0;
        }
        return deathNote.get(tempIndex);
    }

    private LinkedList<EntityLivingBase> getMultiTargets() {
        LinkedList<EntityLivingBase> mList = new LinkedList<>();
        for (Object o : this.getWorld().loadedEntityList) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase)o;
                if (isValid(entityLivingBase)) {
                    mList.add(entityLivingBase);
                }
            }
        }

        Collections.sort(mList, new Comparator<EntityLivingBase>() {
            @Override
            public int compare(EntityLivingBase o1, EntityLivingBase o2) {
                return (int)Helper.getDistanceBetweenAngles((float)getPlayer().getLastYaw(),Helper.getRotationsAtLocation(Location.CHEST,o1)[0]) - (int)Helper.getDistanceBetweenAngles((float)getPlayer().getLastYaw(),Helper.getRotationsAtLocation(Location.CHEST,o2)[0]);
            }
        });
        LinkedList multiEntitys = new LinkedList();
        for (int i = 0; i < 3; i++) {
            if (mList.size() <= i) break;
            multiEntitys.add(mList.get(i));
        }
        if (!mList.isEmpty() &&  multiEntitys.isEmpty()) {
            multiEntitys.add(mList.get(0));
        }




        return multiEntitys;
    }


    private boolean shouldBlock() {
        return getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemSword;
    }

    private void attack(EntityLivingBase target) {


        if (swing.isState())
            getPlayer().swingItem();
        else
            getPlayer().sendPacket(new C0APacketAnimation());

        getPlayer().sendQueue.addToSendQueue(new C02PacketUseEntity(target,C02PacketUseEntity.Action.ATTACK));
        float sharpLevel = EnchantmentHelper.func_152377_a(getPlayer().getHeldItem(), target.getCreatureAttribute());
        boolean vanillaCrit = (getPlayer().fallDistance > 0.0F) && (!getPlayer().onGround) && (!getPlayer().isOnLadder()) && (!getPlayer().isInWater()) && (!this.getPlayer().isPotionActive(Potion.blindness)) && (this.getPlayer().ridingEntity == null);
        if (vanillaCrit || Skuxx.getInstance().getModManager().getMod("Criticals").getState()) {
            getPlayer().onCriticalHit(target);
        }
        if (sharpLevel > 0) {
            getPlayer().onEnchantmentCritical(target);
        }

    }

    private EntityLivingBase getNewTarget() {
        if (deathNote.size() == 0) {
            return null;
        }
        index++;
        return getIndexTarget();

    }

    private int getNextTargetDelay() {
        if (this.deathNote.size() <= 3) {
            return 205;
        }
        float nowRotationYaw = (float)getPlayer().getLastYaw();
        float nowRotationPitch = (float)getPlayer().getLastPitch();
        int x = this.index + 1;
        if (x >= this.deathNote.size()) {
            x = 0;
        }
        EntityLivingBase newTarget = (EntityLivingBase)this.deathNote.get(x);
        float waitdelay = Helper.MovementInput() ? 120.0F : 80.0F;
        float newTargetYaw = Helper.getRotationsAtLocation(Location.LEG,newTarget)[0];
        float newTargetPitch = Helper.getRotationsAtLocation(Location.LEG,newTarget)[1];
        waitdelay += Math.abs(Helper.getDistanceBetweenAngles(nowRotationYaw, newTargetYaw));
        waitdelay += Math.abs(Helper.getDistanceBetweenAngles(nowRotationPitch, newTargetPitch) / 5.0F);
        return (int)waitdelay < 0.0 ? (int)-waitdelay : (int)waitdelay; }



    private EntityLivingBase getIndexTarget() {
        if (index >= deathNote.size()) {
            index = 0;
        }
        return deathNote.get(index);
    }

    private int getNUsed()
    {
        for (int i = 9; i < this.getPlayer().inventoryContainer.inventorySlots.size(); i++) {
            if (this.getPlayer().inventoryContainer.inventorySlots.get(i) != null)
            {
                ItemStack is = (ItemStack)this.getPlayer().inventoryContainer.inventoryItemStacks.get(i);
                if (is == null) {
                    return i;
                }
                Item item = is.getItem();
                if (!hasDamage(item)) {
                    return i;
                }
            }
        }
        return 0;
    }
    private boolean hasDamage(Item item)
    {
        return ((item instanceof ItemSword)) || ((item instanceof ItemAxe)) || ((item instanceof ItemSpade)) || ((item instanceof ItemHoe)) || ((item instanceof ItemPickaxe));
    }



    public boolean isValid(EntityLivingBase e) {

        if (e == null || !e.isEntityAlive()) {
            return false;
        }
        if (e.getDistanceToEntity(getPlayer()) > range.getValue()) {
            return false;
        }
        if (e == getPlayer()) {
            return false;
        }
        if (e instanceof EntityMob && !mobs.isState()) {
            return false;
        }
        if (e instanceof EntityPlayer && !players.isState()) {
            return false;
        }
        if (e == getPlayer().ridingEntity) return false;
        if (e instanceof EntityPlayer && !friend.isState() && Skuxx.getInstance().getFriendManager().isFriend(e.getName())) {
            return false;
        }

        if (e instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable)e;
            if (tameable.getOwner() == getPlayer()) {
                return false;
            }
            if (tameable.isTamed()) {
                for (Friend friend : Skuxx.getInstance().getFriendManager().friendList) {
                    if (friend.getName().equalsIgnoreCase(tameable.getOwner().getName())) {
                        return false;
                    }
                }
            }

        }
        return true;
    }


    public String setMode(String mode) {
        switch (mode.toLowerCase()) {
            case "switch":
                Aura.this.mode = Mode.SWITCH;
                break;
            case "tick":
                Aura.this.mode = Mode.TICK;
                break;
            case "hurttime":
                Aura.this.mode = Mode.HURTTIME;
                break;
            case "aac":
                Aura.this.mode = Mode.AAC;
                break;
            case "multi":
                Aura.this.mode = Mode.MULTI;
                break;
            case "tick2":
                Aura.this.mode = Mode.Tick2;
                break;
            case "crow":
                Aura.this.mode = Mode.Karga;
                break;
        }
        NotificationManager.addNotification(new Notification("AuraMode changed to " + Aura.this.mode.name(),false,10));
        this.mod = Aura.this.mode.name().substring(0,1).toUpperCase() + Aura.this.mode.name().substring(1,Aura.this.mode.name().length()).toLowerCase();
        setSuffix(Aura.this.mode.suffix);
        return "AuraMode changed to " + Aura.this.mode.suffix;
    }

    public enum Mode {
        SWITCH("Switch"), TICK("Tick"), HURTTIME("Hurttime"),AAC("AAC"),MULTI("Multi"),Tick2("Tick2"),Karga("Crow");
        private String suffix;
        Mode(String suffix) {
            this.suffix = suffix;
        }
    }





    private EntityPlayer aacTarget;

    @EventTarget
    private void onMiddle(MiddleClickEvent e) {
        if (Aura.this.mode == Mode.AAC && this.getMinecraft().objectMouseOver != null && getMinecraft().objectMouseOver.entityHit != null && this.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer  && isValid((EntityPlayer) this.getMinecraft().objectMouseOver.entityHit)) {
            EntityPlayer temp = (EntityPlayer) this.getMinecraft().objectMouseOver.entityHit;

            this.aacTarget = temp;
            NotificationManager.addNotification(new Notification("Target set to " + aacTarget.getName(),false,20));


        }
    }

}
