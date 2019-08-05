package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.irc.irccmd.IRCCommand;

public class IRCSetPreference
extends IRCCommand{

    public IRCSetPreference()
    {
        super("setpreference");
    }


    public void run(String text) {
        String name = text.split(" ")[2];
        System.out.println(name);

    }
}
