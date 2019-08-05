package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.skuxx.mods.visual.Gui;

public class Guicolor
extends Command{


    public Guicolor() {
        super(new String[]{"guicolor"},"Changes GUI Color");
    }

    public String execute(String message, String[] split) {

        if (split.length == 0) {
            return "guicolor <color>";
        }
        int color = Integer.parseInt(split[1]);

        return "Gui color changed to " + color;

    }
}
