package xyz.erik.skuxx.mods.move;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.Event;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.EventTick;
import xyz.erik.skuxx.event.events.SprintEvent;
import xyz.erik.skuxx.mods.Category;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

import java.util.Random;

public class Sprint
extends Mod
{

    public Sprint() {
        super("Sprint");
        setCategory(Category.MOVE);
    }
    


    @EventTarget
    private void onTick(EventTick e) {
        if (getPlayer() != null && shouldSprint()) {
            getPlayer().setSprinting(true);
        }

    }

    @EventTarget
    private void onSprint(SprintEvent event) {
        event.sprint = shouldSprint();

    }

    private boolean shouldSprint() {
        return !getPlayer().isSneaking() && !getPlayer().isCollidedHorizontally && (getPlayer().moveStrafing != 0 || getPlayer().moveForward != 0);
    }

}
