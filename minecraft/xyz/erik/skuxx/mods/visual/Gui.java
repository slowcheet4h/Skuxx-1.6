package xyz.erik.skuxx.mods.visual;

import net.minecraft.client.Minecraft;
import xyz.erik.api.mod.Mod;
import xyz.erik.skuxx.ui.click.ClickConsole;
import xyz.erik.skuxx.ui.screens.MusicScreen;
import xyz.erik.skuxx.ui.screens.SkuxxUI;

public class Gui
extends Mod{

    public Gui() {
        setVisible(false);
    }

    public SkuxxUI ui = null;
    public void onEnabled() {

        ui = null;

        getMinecraft().displayGuiScreen(ui == null ? ui = new SkuxxUI() : ui);
        toggle();
    }
}
