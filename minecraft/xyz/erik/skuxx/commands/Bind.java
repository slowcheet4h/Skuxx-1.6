package xyz.erik.skuxx.commands;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;

public class Bind
extends Command{


    public Bind() {
        super(new String[]{"bind","setbind"},"Bind key to a mod");
    }


    public String execute(String message, String[] split) {

        if (split.length < 2) {
            return "bind mod <key>";
        }

        if(split.length == 2) {
            Mod mod = Skuxx.getInstance().getModManager().getMod(split[1].toUpperCase());
            mod.setBind(0);
            return "Bind cleared";
        }

        Mod mod = Skuxx.getInstance().getModManager().getMod(split[1]);
        if (mod.getName() == null)
        {
            return "Mod not found :(";
        }
            mod.setBind(Keyboard.getKeyIndex(split[2].toUpperCase()));
        return mod.getName() +"'s bind changed to " + Keyboard.getKeyName(mod.getBind());
    }
}
