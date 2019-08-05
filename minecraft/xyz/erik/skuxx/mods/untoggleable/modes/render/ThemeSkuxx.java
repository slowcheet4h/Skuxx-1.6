package xyz.erik.skuxx.mods.untoggleable.modes.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.font.FontUtil;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.event.events.EventPacket;
import xyz.erik.skuxx.event.events.KeyPressEvent;
import xyz.erik.skuxx.mods.exploits.IRC;
import xyz.erik.skuxx.mods.untoggleable.Render;
import xyz.erik.skuxx.ui.tabui.Tabgui;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Random;

public class ThemeSkuxx
        extends RenderTheme{


    public static final ResourceLocation LOGO = new ResourceLocation("skuxx/logo.png");
    private Tabgui skuxxTabUI = new Tabgui();
    private ErikTimer timer = new ErikTimer();

    public ThemeSkuxx() {
        EventManager.register(this);

    }

    private double lapinY;
    private boolean side;


    public void render(int height, int width) {
        int xW = 2;
         boolean tabgui = ((Render)Skuxx.getInstance().getModManager().getMod("Render")).tabgui.isState();
        if (tabgui) {
            skuxxTabUI.show();
        }

        int y = 5;
        for (Mod mod:
                Skuxx.getInstance().getModManager().getMods()) {
            if (mod.isVisible() && mod.getState()) {
                if (mod.getSuffix() != " ")
                    Helper.getArrayFont().drawStringWithShadow(mod.getName(), width -  4 -  Helper.getArrayFont().getWidth(mod.getName() + " " + mod.getSuffix() + (mod.getSuffix() != "" ? " " : "")), y,Skuxx.COLOR == -55 ? mod.getModColor() : Skuxx.COLOR);
                else
                    Helper.getArrayFont().drawStringWithShadow(mod.getName(), width -  2-  Helper.getArrayFont().getWidth(mod.getName()), y,Skuxx.COLOR == -55 ? mod.getModColor() : Skuxx.COLOR);

                Helper.getArrayFont().drawStringWithShadow(mod.getSuffix(),width - 5 - Helper.getArrayFont().getWidth(" " +mod.getSuffix()),y,-3355444);
                y += 10;
            }
        }
        drawPing(width,height);
        Helper.getSmallFont().drawStringWithShadow(Skuxx.getInstance().getBuild() + "",xW  - (Helper.getSmallFont().getWidth(Skuxx.getInstance().getBuild() + "") / 2) + 14,17,-3355444);
        Helper.getVerdana().drawStringWithShadow("Skuxx",4,7,-1);

        //        RenderUtil.drawRect(width - 1,5,width,y,0xFFFF0000);

        Helper.getUIFont().drawStringWithShadow("Username:",width - 2 -(int)Helper.getUIFont().getWidth("Username: " + Skuxx.getUser()),height - 10,-1);
        Helper.getUIFont().drawStringWithShadow(Skuxx.getUser(),width - 2 -(int)Helper.getUIFont().getWidth(Skuxx.getUser()),height - 10,-6301338);
        int mY = tabgui ? 65 : 12;
        if(Render.LEAVE_FOCUS >= 0) {
            for (String msg : IRC.lastMessages) {
                Helper.getUIFont().drawStringWithShadow(msg, 2, mY, -1);
                mY += 8;
            }
            Render.LEAVE_FOCUS--;
        } else {
            IRC.lastMessages.clear();
        }
    }

    private int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f).getRGB();
    }




    public void keyPress(KeyPressEvent e) {
        skuxxTabUI.keyDown(e.getKey());
    }

    public void bindTexture(ResourceLocation LOGO, int x,int y, int u, int v, int width, int height, int textureWidth,int textureHeight){

        GL11.glPopMatrix();
        Helper.mc().getTextureManager().bindTexture(LOGO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, textureWidth, textureHeight);
        GL11.glPushMatrix();
    }


    public void drawPing(int width,int height) {

        String text = "|||||||||";
        int color = -16711936;
        if (timer.delay(20000L))
        {
            color = -10092544;
            text = "";
        }
        else if (timer.delay(18000L))
        {
            color = -3407872;
            text = "|";
        }
        else if (timer.delay(13500L))
        {
            color = -65536;
            text = "||";
        }
        else if (timer.delay(10000L))
        {
            color = -1175296;
            text = "|||";
        }
        else if (timer.delay(6000L))
        {
            color = -3394816;
            text = "||||";
        }
        else if (timer.delay(3500L))
        {
            color = -5614336;
            text = "|||||";
        }
        else if (timer.delay(2000L))
        {
            color = -7833856;
            text = "||||||";
        }
        else if (timer.delay(1000L))
        {
            color = -10053376;
            text = "|||||||";
        }
        else if (timer.delay(500L))
        {
            color = -12272896;
            text = "||||||||";
        }
        else if (timer.delay(100L))
        {
            color = -14492416;
            text = "|||||||||";
        }
        else
        {
            color = -16711936;
            text = "||||||||||";
        }
        Helper.getArrayFont().drawStringWithShadow(text + " " + (timer.getDifference() > 500 ? timer.getDifference() : ""),width / 2 + 93,height - 19,color);
    }

    @EventTarget
    private void onPacket(EventPacket e) {
        if (e.getType() == EventPacket.Type.TAKE) {
            timer.reset();
        }
    }
}
