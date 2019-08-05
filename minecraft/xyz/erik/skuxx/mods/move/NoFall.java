package xyz.erik.skuxx.mods.move;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.mods.Category;

public class  NoFall
extends Mod{

    public NoFall() {
        setCategory(Category.MOVE);
        addSet(ground);
        addSet(home);
    }

    private Bool home = new Bool("Home",true);
    private Bool ground = new Bool("Ground",false);

    public void onEnabled() {
        super.onEnabled();
    }

    private ErikTimer timer = new ErikTimer();
    @EventTarget
    private void onMotion(EventPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (home.isState()) {
                setSuffix("Home");
            } else if (ground.isState()) {
                setSuffix("Ground");
                if (!getPlayer().onGround && !getMinecraft().gameSettings.keyBindJump.pressed) {
                    final C03PacketPlayer f = (C03PacketPlayer)event.getPacket();
                    f.setOnGround(true);
                }
            } else {
                setSuffix("SpoofGround");
                final C03PacketPlayer f = (C03PacketPlayer)event.getPacket();
                f.setOnGround(false);

                if (getPlayer().onGround) {
                    f.setY(f.getY() + (getPlayer().ticksExisted % 2 == 0 ? 0.42 : 0 ));
                }
            }
            if(home.isState() &&!getPlayer().onGround && !getPlayer().isInLiquid() && !getPlayer().isOnLiquid() && !getPlayer().isOnLadder() && !getPlayer().isCollidedHorizontally && !getPlayer().isCollidedVertically) {
                if (getPlayer().motionY <= -0.3  &&getPlayer().fallDistance > 4) {
                    event.setCancelled(true);
                    if (timer.delay(1000)) {
                        getPlayer().sendPacket(new C01PacketChatMessage("/sethome"));
                         timer.reset();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    getPlayer().sendPacket(new C01PacketChatMessage("/home"));
                                }catch (InterruptedException e){}
                            }
                        }).start();
                    }
                }

            }
        }
    }
}
