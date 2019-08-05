package xyz.erik.skuxx.mods.move;

import net.minecraft.util.MovementInput;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventPlayerMove;
import xyz.erik.skuxx.mods.Category;

public class AntiWeb
extends
        Mod{

    private int ticks;
    public AntiWeb() {

        setCategory(Category.MOVE);
    }
    @EventTarget
    private void onEventPlayerMove(EventPlayerMove eventPlayerMove) {
        if (getPlayer().isInWeb) {
            ticks++;
            if (this.ticks == 4 && getPlayer().onGround) {
              getPlayer().motionY = 0.22;
              getPlayer().setMoveSpeed(eventPlayerMove,0.8800000059604645D);
            }
            if (this.ticks >= 5) {

                getPlayer().setMoveSpeed(eventPlayerMove, 0.6600000119209289D);
                this.ticks = 0;
            }

        }
    }



}
