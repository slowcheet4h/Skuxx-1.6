package xyz.erik.skuxx.mods.player;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.Category;

public class FastUse
extends Mod
{
    private Bool instant = new Bool("Instant",false);

    public FastUse() {
        addSet(instant);

        setCategory(Category.PLAYER);
    }
    @EventTarget
    private void onMotion(EventMotion event) {

        if (getPlayer().getHeldItem() == null) return;
        if (getPlayer().getHeldItem().getItem() instanceof ItemBow || getPlayer().getHeldItem().getItem() instanceof ItemFood || getPlayer().getHeldItem().getItem() instanceof ItemAppleGold || getPlayer().getHeldItem().getItem() instanceof ItemPotion) {
          if (( instant.isState() ||getPlayer().getItemInUseDuration() >= 16) && getPlayer().isUsingItem()&& getPlayer().onGround) {
              for(int i = Skuxx.getInstance().getModManager().getMod("BowAim").isRunning() ? 16 :  17; i > 0; i--) {
                  getPlayer().sendPacket(new C03PacketPlayer(getPlayer().onGround));
              }

              FastUse.this.minecraft.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
              FastUse.this.minecraft.thePlayer.stopUsingItem();
          }
        }
    }
}
