package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;

public class ItemScale
extends Command{

    public ItemScale() {
        super(new String[]{"is","itemscale"},"Setcale");
    }

    public String execute(String message, String[] split) {
        double scale = Double.parseDouble(split[1]);
        if (Helper.player().getHeldItem() != null && Helper.player().getHeldItem().getItem() != null) {
            Helper.player().getHeldItem().getItem().scale = scale;
        }
        return "";
    }
}
