package xyz.erik.api.font;

import net.minecraft.client.gui.FontRenderer;

public class TextHelper {
    private FontRenderer
            font;

    public TextHelper(FontRenderer font)
    {
        this.font = font;
    }

    public FontRenderer getFont()
    {
        return this.font;
    }

    public float drawString(String text, float x, float y, int color)
    {
        return getFont().func_180455_b(text, x, y, color, false);
    }

    public float drawStringWithShadow(String text, float x, float y, int color, float[] width)
    {
        if ((width[0] > 0.45F) && (width[0] < 0.8F)) {
            width[0] = 0.8F;
        }
        if ((width[1] > 0.45F) && (width[1] < 0.8F)) {
            width[1] = 0.8F;
        }
        float length = 0.0F;
        length = getFont().func_180455_b(text, x + width[0],
                y + width[1], color, true);
        return Math.max(length,
                getFont()
                        .drawString(text, (int)x, (int)y, color));
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color, float[] width)
    {
        return drawStringWithShadow(text, center(text, x), y, color,
                width);
    }

    public float drawStringWithShadow(String text, float x, float y, int color, float width)
    {
        return drawStringWithShadow(text, x, y, color, new float[] {
                width, width });
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color, float width)
    {
        return drawStringWithShadow(text, center(text, x), y, color,
                width);
    }

    public float drawStringWithOutline(String text, float x, float y, int color, float[] width)
    {
        if ((width[0] > 0.45F) && (width[0] < 0.8F)) {
            width[0] = 0.8F;
        }
        if ((width[1] > 0.45F) && (width[1] < 0.8F)) {
            width[1] = 0.8F;
        }
        float length = 0.0F;
        getFont().func_180455_b(text, x + width[0], y, color, true);
        getFont().func_180455_b(text, x - width[0], y, color, true);
        getFont().func_180455_b(text, x, y + width[1], color, true);
        length = getFont().func_180455_b(text, x, y - width[1],
                color, true);
        return Math.max(length,
                getFont().func_180455_b(text, x, y, color, false));
    }

    public float drawCenteredStringWithOutline(String text, float x, float y, int color, float[] width)
    {
        return drawStringWithOutline(text, center(text, x), y, color,
                width);
    }

    public float drawStringWithOutline(String text, float x, float y, int color, float width)
    {
        return drawStringWithOutline(text, x, y, color, new float[] {
                width, width });
    }

    public float drawCenteredStringWithOutline(String text, float x, float y, int color, float width)
    {
        return drawStringWithOutline(text, center(text, x), y, color,
                width);
    }

    public float getStringWidth(String text)
    {
        return getFont().getStringWidth(text);
    }

    public float getStringHeight(String text)
    {
        return getFont().FONT_HEIGHT;
    }

    public float center(String text, float x)
    {
        return x - getFont().getStringWidth(text) / 2;
    }
}
