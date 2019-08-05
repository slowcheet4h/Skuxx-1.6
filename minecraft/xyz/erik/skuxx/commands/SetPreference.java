package xyz.erik.skuxx.commands;

import net.minecraft.client.Minecraft;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.Skuxx;

public class SetPreference
extends Command{

    public SetPreference() {
        super(new String[]{"st","setpreference"},"Customize your player");
    }

    public String execute(String message, String[] split) {

        boolean isChild = false;
        if (split[1] != null) {
            isChild = Boolean.parseBoolean(split[1]);
        }
          Skuxx.getInstance().getSkuxxPlayer().setChild(isChild);
       Skuxx.getInstance().updateSkuxxPlayer();

        return "";
    }
}
