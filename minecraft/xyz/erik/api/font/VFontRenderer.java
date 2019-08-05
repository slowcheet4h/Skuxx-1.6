package xyz.erik.api.font;

import java.awt.*;

public class VFontRenderer {
    public static VFontRenderer createFontRenderer(FontObjectType type)
    {
        VFontRenderer fontRenderer = null;
        switch (type)
        {
            case DEFAULT:
                fontRenderer = new DefaultFontRenderer();
                break;
        }
        return fontRenderer;
    }

    public static enum FontObjectType
    {
        CFONT,  SKID,  NAHR,  DEFAULT;

        private FontObjectType() {}
    }

    public int drawString(String text, float x, float y, int color)
    {

        return 0;
    }

    public int drawStringWithShadow(String text, float x, float y, int color)
    {

        return 0;
    }

    public int getStringWidth(String text)
    {

        return 0;
    }

    public int getHeight()
    {
        return 0;
    }

    public static Font createFontFromFile(String name, int size)
    {
        Font f = null;
        try
        {
            f = Font.createFont(0, new Object().getClass().getResourceAsStream("/" + name + ".ttf"));
        }
        catch (Exception e)
        {
            return null;
        }
        f = f.deriveFont(0, size);

        return f;
    }
}
