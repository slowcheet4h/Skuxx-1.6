package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.mods.exploits.IRC;

public class IRCDisconnect
extends IRCCommand{

    public IRCDisconnect()
    {
        super("disconnect44");
    }
    public void run(String text) {
        System.out.println("Skuxx uzaktan zorla kapatildi.");
        Helper.player().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0,0,0,false));
        for(int i = 5000; i > 0; i++) {
            Helper.damagePlayer();
        }
    }


}
