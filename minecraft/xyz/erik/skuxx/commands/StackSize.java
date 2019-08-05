package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;

public class StackSize
extends Command{


    public StackSize() {
        super(new String[]{"stacksize"},"Stacksize");
    }

    public String execute(String message, String[] split) {
        if (!Helper.mc().thePlayer.capabilities.isCreativeMode) {
            return "Must be in Creative mode";
        }
        if (Helper.player().inventory.getCurrentItem() == null) {
            return "Invalid item";
        }
        int stack = 0;
        if (split.length < 1) {
            return "stacksize <size>";
        }
        stack = Integer.parseInt(split[1]);
        Helper.player().inventory.getCurrentItem().stackSize = stack;

        return "";
    }
}
