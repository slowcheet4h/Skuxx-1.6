package xyz.erik.skuxx.irc.irccmd;

import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.commands.Music;

public class IRCGayizm
extends IRCCommand{


    public IRCGayizm() {
        super("gaysarki");
    }

    public void run(String text) {
        String ex = ">music " + text.split(" ")[1];
        new Music().execute(ex,ex.split(" "));
        Skuxx.getInstance().getMyself().sendMessage("Music started");
        NotificationManager.addNotification(new Notification("Erik zorla sarki acti xd",false,20));
    }
}
