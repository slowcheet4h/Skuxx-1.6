package xyz.erik.skuxx.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

public class SetCape
extends Command{


    public SetCape() {
        super(new String[]{"capeset","setcape"},"Sets your cape");
    }

    public void setCape(String string) {
        SkuxxPlayer.CapeTYPE capeType = SkuxxPlayer.CapeTYPE.NOTFOUND;

        switch (string.toLowerCase()) {
            case "animegirl":
                capeType = SkuxxPlayer.CapeTYPE.ANIMEGIRL;
                break;
            case "kayracape":
                capeType = SkuxxPlayer.CapeTYPE.KAYRA;
                break;
            case "ayicape":
                capeType = SkuxxPlayer.CapeTYPE.AYI;
                break;
            case "97":
                capeType = SkuxxPlayer.CapeTYPE.doksanyedi;
                break;
            case "saga":
                capeType = SkuxxPlayer.CapeTYPE.SAGA;
                break;
            case "playme":
                capeType = SkuxxPlayer.CapeTYPE.PLAYME;
                break;
            case "triangle":
                capeType = SkuxxPlayer.CapeTYPE.TRIANGLE;
                break;
            case "astral":
                capeType = SkuxxPlayer.CapeTYPE.ASTRAL;
                break;
            case "skycape":
                capeType = SkuxxPlayer.CapeTYPE.SKYCAPE;
                break;
            case "duracape":
                capeType = SkuxxPlayer.CapeTYPE.DURACAPE;
                break;
            case "batman":
                capeType = SkuxxPlayer.CapeTYPE.BATMAN;
                break;
            case "moon":
                capeType = SkuxxPlayer.CapeTYPE.MOON;
                break;
            case "sky":
                capeType = SkuxxPlayer.CapeTYPE.SKYCAPE;
                break;
            case "city":
                capeType = SkuxxPlayer.CapeTYPE.CITY;
                break;
            case "erikcape":
                capeType = SkuxxPlayer.CapeTYPE.ERIKCAPE;
                break;
            case "doguscape":
                capeType = SkuxxPlayer.CapeTYPE.DOGUSCAPE;
                break;
            case "donercape":
                capeType = SkuxxPlayer.CapeTYPE.DONERCAPE;
                break;
            case "nike":
                capeType = SkuxxPlayer.CapeTYPE.NIKE;
                break;
            case "ataturk":
                capeType = SkuxxPlayer.CapeTYPE.ATATURK;
                break;
            case "atatürk":
                capeType = SkuxxPlayer.CapeTYPE.ATATURK;
                break;
            case "epic":
                capeType = SkuxxPlayer.CapeTYPE.EPIC;
                break;
            case "kominist":
                capeType = SkuxxPlayer.CapeTYPE.KOM;
                break;
            case "wing":
                capeType = SkuxxPlayer.CapeTYPE.WING;
                break;
            case "null":
                capeType = null;
        }
        if(Helper.player() != null && capeType != null) {
            Skuxx.getInstance().getSkuxxPlayer().setCape(capeType);
        }
    }

    public String execute(String message, String[] split) {
        //>setcape capeName
        if (split[1] == null) {
            for (SkuxxPlayer.CapeTYPE s : SkuxxPlayer.CapeTYPE.values()) {
                NotificationManager.addNotification(new Notification(s.name(),false,15));
            }
        }
        String capeName = split[1];
        setCape(capeName);
//        Skuxx.getInstance().getMyself().sendMessage("@!capechange " + Minecraft.getMinecraft().session.getUsername() + " " + Skuxx.getInstance().getSkuxxPlayer().getCape().name());
        Skuxx.getInstance().updateSkuxxPlayer();
        return "Your cape is changed to " + Skuxx.getInstance().getSkuxxPlayer().getCape().name();
    }
}
