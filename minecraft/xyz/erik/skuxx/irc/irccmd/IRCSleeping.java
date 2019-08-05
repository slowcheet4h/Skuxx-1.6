package xyz.erik.skuxx.irc.irccmd;

import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

public class IRCSleeping
extends IRCCommand{

    public IRCSleeping() {
        super("sleep");
    }

    public void run(String text) {
        String playerName = text.split("::")[1];
        SkuxxPlayer skuxxPlayer = Skuxx.getSkuxxUser(playerName);
        if (skuxxPlayer != null) {
            skuxxPlayer.setSleeping(Boolean.parseBoolean(text.split("::")[2]));
        } else {
            System.out.println("Error on finding skuxx user (IRCSleeping.java line 16)");
        }
    }
}
