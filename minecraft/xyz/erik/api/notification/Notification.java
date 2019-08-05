package xyz.erik.api.notification;

import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.RenderUtil;

public class Notification {

    private String text;
    private boolean warning;
    public int time;

    public Notification(String text, boolean warning,int time)
    {
        this.text = text;
        this.warning = warning;
        this.time = time;
    }


    public void draw(int height, int width)
    {
        if (Helper.mc().currentScreen instanceof GuiChat) return;
        GL11.glPushMatrix();

        Helper.getArrayFont().drawStringWithShadow(text,width - 8 - Helper.getArrayFont().getWidth(text),height - 15,-1);


        GL11.glPopMatrix();
    }

}
