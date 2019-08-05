package xyz.erik.skuxx.commands;

import net.minecraft.client.Minecraft;
import xyz.erik.api.command.Command;
import xyz.erik.skuxx.Skuxx;

public class Sleep
extends Command{

    public Sleep() {
        super(new String[]{"sleep"},"Sleepy");
    }


    public String execute(String message, String[] split) {
        if (split.length < 1) return "sleep <true/false>";
        boolean sleepState = Boolean.parseBoolean(message.split(" ")[1]);
        Skuxx.getInstance().getSkuxxPlayer().setSleeping(sleepState);
        Skuxx.getInstance().getMyself().sendMessage("@!sleep::"+ Minecraft.getMinecraft().session.getUsername() +"::"+sleepState);
        String text = "You are " + (sleepState ? "sleeping" : "awake") + " now";
        return text;
    }
}
