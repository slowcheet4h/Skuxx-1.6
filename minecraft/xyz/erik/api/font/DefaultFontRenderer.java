package xyz.erik.api.font;

import net.minecraft.client.gui.FontRenderer;
import xyz.erik.api.helpers.Helper;

public class DefaultFontRenderer
extends VFontRenderer{
    private FontRenderer fontRenderer;

    public DefaultFontRenderer()
    {
        this.fontRenderer = Helper.mc().fontRendererObj;
    }

    public int drawString(String text, float x, float y, int color)
    {
        return this.fontRenderer.drawString(text, (int)x, (int)y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color)
    {
        return this.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public int getStringWidth(String text)
    {
        return this.fontRenderer.getStringWidth(text);
    }

    public int getHeight()
    {
        return this.fontRenderer.FONT_HEIGHT;
    }
}
