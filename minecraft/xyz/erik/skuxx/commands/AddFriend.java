package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;

public class AddFriend
extends Command{

    public AddFriend() {
        super(new String[]{"friend"},"Add Friend");
    }

    public String execute(String message, String[] split) {
        //f add isim
        if(split[1].toLowerCase().equals("remove")) {
            Skuxx.getInstance().getFriendManager().removeFriend(split[2]);

            NotificationManager.addNotification(new Notification("Friend removed " + split[2],false,25));
        } else if(split[1].toLowerCase().equals("add")) {
            String alias = split[2];
            if (split.length >= 4) alias = split[3];
            Skuxx.getInstance().getFriendManager().addFriend(split[2],alias);
            NotificationManager.addNotification(new Notification("Friend added " + alias,false,25));
        } else {
            NotificationManager.addNotification(new Notification("add/remove",false,25));
        }
        return "";
    }
}
