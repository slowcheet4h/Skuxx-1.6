package xyz.erik.skuxx.mods.move;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.AxisAlignedBB;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.player.Player;
import xyz.erik.skuxx.event.Event.Motion;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.StepEvent;
import xyz.erik.skuxx.mods.Category;

public class Step
  extends Mod
{
  private int fix;
  
  public Step()
  {
    setCategory(Category.MOVE);
    addSet(this.birbucuk);
  }
  
  private ErikTimer erikTimer = new ErikTimer();
  private double oldY;
  private Bool birbucuk = new Bool("1.5", true);
  
  @EventTarget
  private void onStep(StepEvent event)
  {
    if ((!isMoving()) || (!getPlayer().onGround) || (getPlayer().isOnLiquid()) || (getPlayer().isOnLiquid()) || (getPlayer().isInLiquid()) || (getPlayer().isOnLadder()) || (!this.erikTimer.delay(250.0F))) {
      return;
    }
    switch (event.getTime())
    {
    case BEFORE: 
      if (this.fix == 0)
      {
        this.oldY = getPlayer().posY;
        event.setHeight(canStep() ? 1.0F : this.birbucuk.isState() ? 1.5F : 0.5F);
      }
      break;
    case AFTER: 
      double offset = this.minecraft.thePlayer.getEntityBoundingBox().minY - this.oldY;
      if ((offset > 0.6D) && (offset <= 1.0D) && (this.fix == 0) && (canStep()))
      {
        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.4099999964237213D, getPlayer().posZ, true));
        getPlayer().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, true));
        this.erikTimer.reset();
        setRunning(2);
        this.fix = 2;
      }
      if ((this.fix == 0) && (canStep()) && (offset < 2.0D) && (offset > 1.0D) && (this.birbucuk.isState()))
      {
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.42D, getPlayer().posZ, getPlayer().onGround));
        
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 0.75D, getPlayer().posZ, getPlayer().onGround));
        
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.0D, getPlayer().posZ, getPlayer().onGround));
        
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.16D, getPlayer().posZ, getPlayer().onGround));
        
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.25D, getPlayer().posZ, getPlayer().onGround));
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));
        
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.3D, getPlayer().posZ, getPlayer().onGround));
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.2D, getPlayer().posZ, getPlayer().onGround));
        setRunning(1);
        getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().posY + 1.4D, getPlayer().posZ, getPlayer().onGround));
        this.fix = 1;
        getMinecraft().getTimer().timerSpeed = 0.3F;
        new Thread(new Runnable()
        {
          public void run()
          {
            try
            {
              Thread.sleep(300L);
              Step.this.getMinecraft().getTimer().timerSpeed = 1.0F;
            }
            catch (InterruptedException localInterruptedException) {}
          }
        })
        
          .start();
      }
      else if ((canStep()) && (offset >= 2.0D) && (offset < 2.5D))
      {
        getPlayer().setPositionAndUpdate(getPlayer().posX, this.oldY, getPlayer().posZ);
        
        getPlayer().jump();
      }
      break;
    }
  }
  
  @EventTarget
  private void Motion(EventMotion eventMotion)
  {
    if ((eventMotion.getMotion() == Event.Motion.BEFORE) && 
      (this.fix > 0))
    {
      eventMotion.setCancelled(true);
      this.fix -= 1;
    }
  }
  
  private boolean canStep()
  {
    return (!getPlayer().isOnLiquid()) && (!getPlayer().isInLiquid()) && (this.minecraft.thePlayer.onGround) && (!this.minecraft.gameSettings.keyBindJump.getIsKeyPressed()) && (this.minecraft.thePlayer.isCollidedVertically) && (this.minecraft.thePlayer.isCollidedHorizontally);
  }
}
