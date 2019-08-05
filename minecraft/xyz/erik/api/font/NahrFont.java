package xyz.erik.api.font;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.helpers.Helper;

public class NahrFont {
    private Font font;
    private boolean antiAlias = true;
    private Graphics2D graphics;
    private FontMetrics metrics;
    private int size;
    private int start;
    private int end;
    private float[] x;
    private float[] y;
    private BufferedImage image;
    private float spacing = 0.0F;
    private DynamicTexture texture;
    private ResourceLocation location;
    private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]");
    private final Pattern patternUnsupported = Pattern.compile("(?i)\\u00A7[K-O]");
    private String name;
    private boolean defaultAntiAlias;
    private float defaultSpacing;
    private Font defaultFont;
    private int defaultSize;

    public NahrFont(String name, Object font, int size) {
        this(name, font, size, 0.0F, true);
    }

    public NahrFont(String name, Object font, int size, boolean antiAlias) {
        this(name, font, size, 0.0F, antiAlias);
    }

    public NahrFont(String name, Object font, int size, float spacing, boolean antiAlias) {
        if (!this.def) {
            this.defaultSize = size;
            this.defaultSpacing = spacing;
            this.defaultAntiAlias = antiAlias;
            this.def = true;
        }
        this.name = name;
        this.size = size;
        this.start = 32;
        this.end = 255;
        this.spacing = spacing;
        this.x = new float[this.end - this.start];
        this.y = new float[this.end - this.start];
        this.antiAlias = antiAlias;
        setupGraphics2D();
        createFont(font, size);
    }

    public NahrFont(String name, Object font, int size, float spacing, boolean antiAlias, boolean enabled) {
        this(name, font, size, spacing, antiAlias);
        set(enabled);
    }

    private void setupGraphics2D() {
        this.image = new BufferedImage(256, 256, 2);
        this.graphics = ((Graphics2D) this.image.getGraphics());
        if (this.antiAlias) {
            this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }

    private void createFont(Object font, float size) {
        try {
            if ((font instanceof Font)) {
                this.font = ((Font) font);
            } else if ((font instanceof File)) {
                this.font = Font.createFont(0, (File) font).deriveFont(size);
            } else if ((font instanceof InputStream)) {
                this.font = Font.createFont(0, (InputStream) font).deriveFont(size);
            } else if ((font instanceof String)) {
                this.font = new Font((String) font, 0, Math.round(size));
            } else {
                this.font = new Font("Verdana", 0, Math.round(size));
            }
            if (this.defaultFont == null) {
                this.defaultFont = this.font;
            }
            this.graphics.setFont(this.font);
        } catch (Exception e) {
            e.printStackTrace();
            this.font = new Font("Verdana", 0, Math.round(size));

            this.graphics.setFont(this.font);
        }
        this.graphics.setColor(new Color(255, 255, 255, 0));
        this.graphics.fillRect(0, 0, 256, 256);
        this.graphics.setColor(Color.white);
        this.metrics = this.graphics.getFontMetrics();

        float x = 5.0F;
        float y = 5.0F;
        for (int i = this.start; i < this.end; i++) {
            this.graphics.drawString(Character.toString((char) i), x, y + this.metrics.getAscent());
            this.x[(i - this.start)] = x;
            this.y[(i - this.start)] = (y - this.metrics.getMaxDescent());
            x += this.metrics.stringWidth(Character.toString((char) i)) + 2.0F;
            if (x >= 250 - this.metrics.getMaxAdvance()) {
                x = 5.0F;
                y += this.metrics.getMaxAscent() + this.metrics.getMaxDescent() + this.size / 2.0F;
            }
        }
        this.location = Helper.mc().getTextureManager().getDynamicTextureLocation("font" + font.toString() + size, this.texture = new DynamicTexture(this.image));
    }

    public void drawString(String text, float x, float y, int color) {

        if (enabled()) {
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            drawer(text, x, y, color);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.popMatrix();
        }
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            drawString(text, x, y, color);
        } else {
            drawStringWithShadow(text, x, y, color, -13355980, 1.0F);
        }
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, center(text, x), y, color);
    }

    public void drawStringWithShadow(String text, float x, float y, int color, int color2, float width) {
        if (enabled()) {
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            String text2 = stripControlCodes(text);

            drawer(text2, x + width, y + width, color2);

            drawer(text, x, y, color);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.popMatrix();
        } else {
            Helper.mc().fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
    }

    public void drawCenteredStringWithShadow(String text, float x, float y, int color, int color2, float width) {
        drawStringWithShadow(text, center(text, x), y, color, color2, width);
    }

    public void drawStringWithOutline(String text, float x, float y, int color, int color2, float width) {
        if (enabled()) {
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            String text2 = stripControlCodes(text);

            drawer(text2, x + width, y + width, color2);
            drawer(text2, x - width, y + width, color2);
            drawer(text2, x, y + width, color2);
            drawer(text2, x, y - width, color2);

            drawer(text, x, y, color);
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.popMatrix();
        }
    }

    public void drawCenteredStringWithOutline(String text, float x, float y, int color, int color2, float width) {
        drawStringWithOutline(text, center(text, x), y, color, color2, width);
    }

    public float center(String text, float x) {
        return x - getStringWidth(text) / 2.0F;
    }

    private void drawer(String text, float x, float y, int color) {
        y -= 5.0F;
        x *= 2.0F;
        y *= 2.0F;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Helper.mc().getTextureManager().bindTexture(this.location);
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        float startX = x;
        for (int i = 0; i < text.length(); i++) {
            int colorCode;
            if ((text.charAt(i) == '�') && (i + 1 < text.length())) {
                char oneMore = Character.toLowerCase(text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += this.metrics.getAscent() + 2;
                    x = startX;
                }
                colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                if (colorCode < 16) {
                    try {
                        int newColor =Helper.mc().fontRendererObj.colorCode[colorCode];
                        GlStateManager.color((newColor >> 16) / 255.0F, (newColor >> 8 & 0xFF) / 255.0F, (newColor & 0xFF) / 255.0F, alpha);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (oneMore == 'f') {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
                } else if (oneMore == 'r') {
                    GlStateManager.color(red, green, blue, alpha);
                } else if (oneMore == 'g') {
                    GlStateManager.color(0.278F, 0.608F, 0.867F, alpha);
                }
                i++;
            } else {
                try {
                    char c = text.charAt(i);
                    drawChar(c, x, y);
                    x += getStringWidth(Character.toString(c)) * 2.0F;
                } catch (ArrayIndexOutOfBoundsException indexException) {
                    colorCode = text.charAt(i);
                }
            }
        }
        GlStateManager.popMatrix();
    }

    public float getStringWidth(String text) {

        if (enabled()) {
            try {
                return (float) (getBounds(text).getWidth() + this.spacing) / 2.0F;
            } catch (Exception e) {
                return -1.0F;
            }
        }
        return -1;
    }

    public float getStringHeight(String text) {

        if (enabled()) {
            try {
                return (float) getBounds(text).getHeight() / 2.0F;
            } catch (Exception e) {
                return 0.0F;
            }
        }
        return- -1;
    }

    private Rectangle2D getBounds(String text) {
        return this.metrics.getStringBounds(StringUtils.stripControlCodes(text), this.graphics);
    }

    private void drawChar(char character, float x, float y)
            throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.metrics.getStringBounds(Character.toString(character), this.graphics);
        drawTexturedModalRect(x, y, this.x[(character - this.start)], this.y[(character - this.start)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.metrics.getMaxDescent() + 1.0F);
    }

    private List listFormattedStringToWidth(String s, int width) {
        return Arrays.asList(wrapFormattedStringToWidth(s, width).split("\n"));
    }

    String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = sizeStringToWidth(s, width);
        if (s.length() <= wrapWidth) {
            return s;
        }
        String split = s.substring(0, wrapWidth);
        String split2 = getFormatFromString(split) + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ') || (s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return split + "\n" + wrapFormattedStringToWidth(split2, width);
        } catch (Exception localException) {
        }
        return "";
    }

    private int sizeStringToWidth(String par1Str, float par2) {
        int var3 = par1Str.length();
        float var4 = 0.0F;
        int var5 = 0;
        int var6 = -1;
        for (boolean var7 = false; var5 < var3; var5++) {
            char var8 = par1Str.charAt(var5);
            switch (var8) {
                case '\n':
                    var5--;
                    break;
                case '�':
                    if (var5 < var3 - 1) {
                        var5++;
                        char var9 = par1Str.charAt(var5);
                        if ((var9 != 'l') && (var9 != 'L')) {
                            if ((var9 == 'r') || (var9 == 'R') || (isFormatColor(var9))) {
                                var7 = false;
                            }
                        } else {
                            var7 = true;
                        }
                    }
                    break;
                case ' ':
                    var6 = var5;
                case '-':
                    var6 = var5;
                case '_':
                    var6 = var5;
                case ':':
                    var6 = var5;
                default:
                    String text = String.valueOf(var8);
                    var4 += getStringWidth(text);
                    if (var7) {
                        var4 += 1.0F;
                    }
                    break;
            }
            if (var8 == '\n') {
                var5++;
                var6 = var5;
            } else {
                if (var4 > par2) {
                    break;
                }
            }
        }
        return (var5 != var3) && (var6 != -1) && (var6 < var5) ? var6 : var5;
    }

    private String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();
        while ((var2 = par0Str.indexOf('�', var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = par0Str.charAt(var2 + 1);
                if (isFormatColor(var4)) {
                    var1 = "�" + var4;
                } else if (isFormatSpecial(var4)) {
                    var1 = var1 + "�" + var4;
                }
            }
        }
        return var1;
    }

    private boolean isFormatColor(char par0) {
        return ((par0 >= '0') && (par0 <= '9')) || ((par0 >= 'a') && (par0 <= 'f')) || ((par0 >= 'A') && (par0 <= 'F'));
    }

    private boolean isFormatSpecial(char par0) {
        return ((par0 >= 'k') && (par0 <= 'o')) || ((par0 >= 'K') && (par0 <= 'O')) || (par0 == 'r') || (par0 == 'R');
    }

    private void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();
        var10.startDrawingQuads();
        var10.addVertexWithUV(x + 0.0F, y + height, 0.0D, (textureX + 0.0F) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + height, 0.0D, (textureX + width) * var7, (textureY + height) * var8);
        var10.addVertexWithUV(x + width, y + 0.0F, 0.0D, (textureX + width) * var7, (textureY + 0.0F) * var8);
        var10.addVertexWithUV(x + 0.0F, y + 0.0F, 0.0D, (textureX + 0.0F) * var7, (textureY + 0.0F) * var8);
        var9.draw();
    }

    private String stripControlCodes(String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }

    private String stripUnsupported(String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }

    private Graphics2D getGraphics() {
        return this.graphics;
    }

    public Font getFont() {
        return this.font;
    }

    public int getSize() {
        return this.size;
    }

    public void set(Font font, int size, float spacing, boolean antiAlias, boolean enabled) {
        this.size = size;
        this.spacing = spacing;
        this.antiAlias = antiAlias;
        this.enabled = enabled;
        setupGraphics2D();
        createFont(font, size);
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public boolean getAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }

    public String name() {
        return this.name;
    }

    public boolean defaultAntiAlias() {
        return this.defaultAntiAlias;
    }

    public float defaultSpacing() {
        return this.defaultSpacing;
    }

    public Font defaultFont() {
        return this.defaultFont;
    }

    public int defaultSize() {
        return this.defaultSize;
    }

    private boolean def = false;
    private boolean enabled = true;

    public boolean enabled() {
        return this.enabled;
    }

    public void set(boolean enabled) {
        this.enabled = enabled;
    }
}