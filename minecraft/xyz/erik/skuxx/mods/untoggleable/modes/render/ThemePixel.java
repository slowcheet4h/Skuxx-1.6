package xyz.erik.skuxx.mods.untoggleable.modes.render;

import org.lwjgl.opengl.GL11;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.tabgui.PixelTabgui;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.events.KeyPressEvent;

public class ThemePixel
extends RenderTheme{


    private PixelTabgui tabGui;

    public ThemePixel() {
        this.tabGui = new PixelTabgui();
    }


    public void render(int height, int width) {
        tabGui.onRender(height,width);
        GL11.glPushMatrix();
        GL11.glScalef(2,2,2);
   //     Helper.getSkuxxFont().drawStringWithShadow("S",2,1,0x4B0082);
        GL11.glPopMatrix();

    //    Helper.getSkuxxFont().drawStringWithShadow("kuxx", Helper.getSkuxxFont().getWidth("S") * 3,9,-3355444);

        int y = 2;
        for (Mod mod:
                Skuxx.getInstance().getModManager().getMods()) {
            if (mod.isVisible() && mod.getState()) {
                Helper.getSegaui().drawStringWithShadow(mod.getName(), width - 3 - Helper.getSegaui().getWidth(mod.getName()), y - 3,   -3355444);
                y += 10;
            }
        }

    }

    public void keyPress(KeyPressEvent e) {
        tabGui.onKey(e);
    }
}
