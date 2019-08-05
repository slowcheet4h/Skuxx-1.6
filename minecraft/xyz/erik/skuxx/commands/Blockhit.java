package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.cfg.SkuxxConfig;

public class Blockhit
extends Command{


    public Blockhit() {
        super(new String[]{"setblock","blockhit","sb","setblockhit"},"Set blockhit animation");
    }

    public String execute(String message, String[] split) {

        if (split.length >= 2) {

            switch (split[1].toLowerCase()) {
                case "skuxx":
                    SkuxxConfig.setBlockhit(xyz.erik.skuxx.enums.Blockhit.Skuxx);
                break;
                case "exeter":
                    SkuxxConfig.setBlockhit(xyz.erik.skuxx.enums.Blockhit.Exeter);
                    break;
                case "sigma":
                    SkuxxConfig.setBlockhit(xyz.erik.skuxx.enums.Blockhit.Sigma);
                break;
            }

        }
        NotificationManager.addNotification(new Notification("Blockhit changed to " + SkuxxConfig.getBlockhit().name(),false,25));
        return null;
    }
}
