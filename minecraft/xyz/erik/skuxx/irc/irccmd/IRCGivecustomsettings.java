package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.Minecraft;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

public class IRCGivecustomsettings
extends IRCCommand{

    public IRCGivecustomsettings() {
        super("givecustomsetting");
    }

    public void run(String text) {
            String setting = "@!getinformation " + Skuxx.getUser() + "::" + Minecraft.getMinecraft().session.getUsername() +"::"+ Skuxx.getInstance().getSkuxxPlayer().isChild()+"::"+Skuxx.getInstance().getSkuxxPlayer().isHaveEar() + "::" + Skuxx.getInstance().getSkuxxPlayer().isRiding()
                    +"::"+ Skuxx.getInstance().getSkuxxPlayer().getCape().name();
            Skuxx.getInstance().getMyself().sendMessage(setting);
            System.out.println("Informations sended succesfully...");

    }
}
