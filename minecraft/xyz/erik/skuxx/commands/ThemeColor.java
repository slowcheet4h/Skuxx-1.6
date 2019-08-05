package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;

public class ThemeColor
extends Command{

    public ThemeColor() {
        super(new String[]{"themecolor"},"Changes theme color");
    }
    public String execute(String message, String[] split) {
        if (split.length < 2) {
            for (ThemeColorEnum c : ThemeColorEnum.values()) {
                NotificationManager.addNotification(new Notification(c.colorName,false,25));
            }
            return "";
        }
        if (split[1].equalsIgnoreCase("random")) {
            Skuxx.COLOR = -55;
            return ".";
        } else {

            for (ThemeColorEnum c : ThemeColorEnum.values()) {
                if(split[1].equalsIgnoreCase(c.colorName)) {
                    Skuxx.COLOR = c.color;
                    NotificationManager.addNotification(new Notification("Themecolor changed to " + c.colorName,false,15));
                    return "";
                }
            }

        }
        return "Color not found";

    }



    public enum ThemeColorEnum {
        WHITE("White",-1),
        GRAY("Gray",0x828282),
        PURPLE("Purple",-9679466),
        DARK_PURPLE("DarkPurple",0x380474),
        MEDIUM_PURPLE("MediumPurple",0x9d13c9),
        PINK("Pink",0xf520fe),
        ORANGE("Orange",0xfc9a57),
        BLACK("Black",0x000000),
        GREEN("Green",0x43e41a);
        int color;
        String colorName;

        ThemeColorEnum(String colorName,int color) {
            this.color = color;
            this.colorName = colorName;
        }
    }
}
