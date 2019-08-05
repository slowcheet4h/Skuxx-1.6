package xyz.erik.skuxx.mods.untoggleable;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.tabgui.PixelTabgui;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventRender;
import xyz.erik.skuxx.event.events.KeyPressEvent;
import xyz.erik.skuxx.mods.Category;
import xyz.erik.skuxx.mods.untoggleable.modes.render.RenderTheme;
import xyz.erik.skuxx.mods.untoggleable.modes.render.ThemeSkuxx;

import java.util.Collections;
import java.util.Comparator;

public class Render extends Mod{

    private RenderTheme theme;
    public static int LEAVE_FOCUS = 0;
    public Bool tabgui = new Bool("Tabgui",false);
    public Render() {
        addSet(tabgui);
        tabgui.setState(false);
        setCategory(Category.VISUAL);
        theme = new ThemeSkuxx();
        if (!getState())
        {
            this.toggle();
        }
        this.setVisible(false);
        theme.onStart();
        new PixelTabgui();
    }
    @EventTarget
    private void onRender(EventRender eventRender) {
        Collections.sort(Skuxx.getInstance().getModManager().getMods(), new Comparator<Mod>()
        {
            public int compare(Mod mod1, Mod mod2)
            {
                String s1 = mod2.getName();
                String s2 = mod1.getName();
                int cmp = (int)Helper.getArrayFont().getWidth(s2 + mod1.getSuffix() + (mod1.getSuffix() != "" ? " " : "")) - (int)Helper.getArrayFont().getWidth(s1 + mod2.getSuffix() + (mod2.getSuffix() != "" ? " " : ""));
                return cmp != 0 ? cmp : s2.compareTo(s1);
            }
        });
        if (Helper.mc().currentScreen != null) return;

        GL11.glPushMatrix();
        theme.render(eventRender.getHeight(),eventRender.getWidth());
        GL11.glPopMatrix();

        //Helper.getAnka().drawStringWithShadow("Skuxx",2,2,-1);


    }


    @EventTarget
    private void onKeyPres(KeyPressEvent e){
        theme.keyPress(e);
    }



}
