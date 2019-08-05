package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

public class IRCgetinformation
extends IRCCommand{

    public IRCgetinformation() {
        super("getinformation");
    }


    public void run(String text) {
        String[] split = text.split("getinformation ")[1].split("::");
        String sName = split[0];
        String ingameName = split[1];
        boolean child = Boolean.parseBoolean(split[2]);
        boolean ear = Boolean.parseBoolean(split[3]);
        boolean riding = Boolean.parseBoolean(split[4]);
        SkuxxPlayer.CapeTYPE cape = getCape(split[5]);
        if (Skuxx.getSkuxxUser(sName) == null) {
            SkuxxPlayer skuxxPlayer = new SkuxxPlayer();
            skuxxPlayer.setName(sName);
            skuxxPlayer.setIngameName(ingameName);
            Skuxx.skuxxPlayerList.add(skuxxPlayer);
        }
        Skuxx.getSkuxxUser(sName).set(ingameName,child,ear,riding,cape);
    }

    public SkuxxPlayer.CapeTYPE getCape(String string) {
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
            case "97    ":
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
        return capeType;
    }
}
