package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.Skuxx;

public class Toggle
extends Command{

    public Toggle() {
        super(new String[] {"t","toggle","tog"},"Toggle Module");
    }

    public String execute(String message, String[] split) {
        if (split[1] != null) {
            Mod mod = Skuxx.getInstance().getModManager().getMod(split[1]);
            if (mod !=null  ) {
                mod.toggle();
                return (mod.getName() + " is " + (mod.getState() ? "enabled" : "disabled"));
            }
        }
        return "Module not found";
    }
}
