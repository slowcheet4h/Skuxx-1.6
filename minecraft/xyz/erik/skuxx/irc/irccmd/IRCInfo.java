package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.Minecraft;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.Skuxx;

public class IRCInfo
extends IRCCommand
{

    public IRCInfo() {
        super("!!!info");
    }

    public void run(String text) {
        String t = "-> " + Skuxx.getInstance().getBuild();
        if (Minecraft.getMinecraft().getCurrentServerData() != null) {
            t += Minecraft.getMinecraft().getCurrentServerData().serverIP;
        }
        if (Minecraft.getMinecraft().thePlayer != null) {
            t += "X: " + Helper.player().posX + " Y:" + Helper.player().posY + " Z:" + Helper.player().posZ;
        }
        Skuxx.getInstance().getMyself().sendMessage(t);
        super.run(text);
    }
}
