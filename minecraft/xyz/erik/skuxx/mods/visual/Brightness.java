package xyz.erik.skuxx.mods.visual;

import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.mods.Category;

public class Brightness
extends Mod{


    public Brightness() {
        setCategory(Category.VISUAL);
    }

    public void onEnabled() {
        while(getMinecraft().gameSettings.gammaSetting < 10)
        getMinecraft().gameSettings.gammaSetting += 1;
        super.onEnabled();
    }

    public void onDisabled() {
        while (getMinecraft().gameSettings.gammaSetting > 0) {
            getMinecraft().gameSettings.gammaSetting -= 1;
        }
        super.onDisabled();
    }
}
