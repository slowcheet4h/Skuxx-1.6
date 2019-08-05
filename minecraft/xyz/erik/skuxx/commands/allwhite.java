package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;

public class allwhite
extends Command{

    public allwhite() {
        super(new String[]{"allwhite"},"Makes arraylist white");
    }


    public String execute(String message, String[] split) {
        boolean on = (split[1].equalsIgnoreCase("on"));
        if (on) {
            for (Mod mod : Skuxx.getInstance().getModManager().getMods()) {
                mod.setModColor(-1);
            }
        } else {
            Skuxx.getInstance().getModManager().loadColors();
        }
        return "Done!";
    }
}
