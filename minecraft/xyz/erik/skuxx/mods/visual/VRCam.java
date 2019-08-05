package xyz.erik.skuxx.mods.visual;

import xyz.erik.api.mod.Mod;

public class VRCam
extends Mod{

    public void onEnabled() {
        getMinecraft().gameSettings.thirdPersonView = 1;
        super.onEnabled();
    }

    public void onDisabled() {

        getMinecraft().gameSettings.thirdPersonView = 0;
        super.onDisabled();
    }
}
