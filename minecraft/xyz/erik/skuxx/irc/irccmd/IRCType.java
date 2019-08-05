package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.network.play.client.C01PacketChatMessage;
import xyz.erik.api.helpers.Helper;

public class IRCType
extends IRCCommand{

    public IRCType() {
        super("irctype");
    }


    public void run(String text) {
        String type = text.split("irctype ")[1];
        Helper.player().sendPacket(new C01PacketChatMessage(type));
    }
}
