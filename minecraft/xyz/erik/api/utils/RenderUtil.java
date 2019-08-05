package xyz.erik.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import sun.plugin2.util.ColorUtil;

import java.awt.*;

public class RenderUtil {


    public static void enableGL3D() {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
    }
    public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2)
    {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, int inside, int border)
    {
        enableGL2D();
        drawRect(x, y, x1, y1, inside);
        glColor(border);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(3);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        disableGL2D();
    }
    public static void drawRect(float x, float y, float x1, float y1, int color)
    {
        enableGL2D();
        glColor(color);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }


    public static int transparency(int color, double alpha)
    {
        Color c = new Color(color);
        float r = 0.003921569F * c.getRed();
        float g = 0.003921569F * c.getGreen();
        float b = 0.003921569F * c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }

    public static float[] getRGBA(int color)
    {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        return new float[] { r, g, b, a };
    }


    public static void texturedRect(double x1, double y1, double x2, double y2, int color)
    {
        float[] rgba = getRGBA(color);
        preRender(rgba);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer render = tess.getWorldRenderer();

        GlStateManager.enableTexture2D();

        render.startDrawingQuads();
        render.addVertexWithUV((int)x1, (int)y2, 0.0D, 0.0D, 1.0D);
        render.addVertexWithUV((int)x2, (int)y2, 0.0D, 1.0D, 1.0D);
        render.addVertexWithUV((int)x2, (int)y1, 0.0D, 1.0D, 0.0D);
        render.addVertexWithUV((int)x1, (int)y1, 0.0D, 0.0D, 0.0D);
        tess.draw();

        postRender();
    }

    public static void preRender(float[] rgba)
    {
        GlStateManager.alphaFunc(516, 0.001F);
        GlStateManager.color(rgba[0], rgba[1], rgba[2], rgba[3]);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void postRender()
    {
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glLineWidth(1.0F);
    }


    public static void drawRect(float x, float y, float x1, float y1)
    {
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void rect(double x1, double y1, double x2, double y2, int color)
    {
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        float a = (color >> 24 & 0xFF) / 255.0F;

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        wr.startDrawingQuads();
        wr.addVertex(x1, y2, 0.0D);
        wr.addVertex(x2, y2, 0.0D);
        wr.addVertex(x2, y1, 0.0D);
        wr.addVertex(x1, y1, 0.0D);
        t.draw();
        GlStateManager.func_179098_w();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
    }

    public static void glColor(int hex)
    {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }
    public static void drawRect(float x, float y, float x1, float y1, float r, float g, float b, float a)
    {
        enableGL2D();
        GL11.glColor4f(r, g, b, a);
        drawRect(x, y, x1, y1);
        disableGL2D();
    }

    public static void disableLighting()
    {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
    }


    public static void drawBorderedRect(float posX, float posY, float posX2, float posY2, float width, int color, int color2, boolean center)
    {
        drawRect(posX, posY, posX2, posY2, color2);
        drawHollowRect(posX, posY, posX2, posY2, width, color, center);
    }
    public static void drawHollowRect(float posX, float posY, float posX2, float posY2, float width, int color, boolean center)
    {
        float corners = width / 2.0F;
        float side = width / 2.0F;
        if (center)
        {
            drawRect(posX - side, posY - corners, posX + side, posY2 + corners, color);
            drawRect(posX2 - side, posY - corners, posX2 + side, posY2 + corners, color);
            drawRect(posX - corners, posY - side, posX2 + corners, posY + side, color);
            drawRect(posX - corners, posY2 - side, posX2 + corners, posY2 + side, color);
        }
        else
        {
            drawRect(posX - width, posY - corners, posX, posY2 + corners, color);
            drawRect(posX2, posY - corners, posX2 + width, posY2 + corners, color);
            drawRect(posX - corners, posY - width, posX2 + corners, posY, color);
            drawRect(posX - corners, posY2, posX2 + corners, posY2 + width, color);
        }
    }


    public static void setColor(Color c)
    {
        GL11.glColor4d(c.getRed() / 255.0F, c.getGreen() / 255.0F, c
                .getBlue() / 255.0F, c.getAlpha() / 255.0F);
    }


    public static void drawLines(AxisAlignedBB mask)
    {
        GL11.glPushMatrix();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.maxY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.maxY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glVertex3d(mask.maxX, mask.minY,
                mask.minZ);
        GL11.glVertex3d(mask.minX, mask.minY,
                mask.maxZ);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    public static void drawFilledBox(AxisAlignedBB mask)
    {
        WorldRenderer worldRenderer = Tessellator.instance.getWorldRenderer();
        Tessellator tessellator = Tessellator.instance;
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        worldRenderer.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.minX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        worldRenderer.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        worldRenderer.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        tessellator.draw();
    }


    public static void drawOutlinedBoundingBox(AxisAlignedBB mask)
    {
        WorldRenderer var2 = Tessellator.instance.getWorldRenderer();
        Tessellator var1 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(mask.minX, mask.minY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.minY,
                mask.minZ);
        var1.draw();
        var2.startDrawing(3);
        var2.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        var1.draw();
        var2.startDrawing(1);
        var2.addVertex(mask.minX, mask.minY,
                mask.minZ);
        var2.addVertex(mask.minX, mask.maxY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.minY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.maxY,
                mask.minZ);
        var2.addVertex(mask.maxX, mask.minY,
                mask.maxZ);
        var2.addVertex(mask.maxX, mask.maxY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.minY,
                mask.maxZ);
        var2.addVertex(mask.minX, mask.maxY,
                mask.maxZ);
        var1.draw();
    }
    public static void drawRoundedRect(final float x, final float y, final float x1, final float y1, final int borderC, final int insideC) {
        drawRect(x + 0.5f, y, x1 - 0.5f, y + 0.5f, insideC);
        drawRect(x + 0.5f, y1 - 0.5f, x1 - 0.5f, y1, insideC);
        drawRect(x, y + 0.5f, x1, y1 - 0.5f, insideC);
    }

    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float)(6.2831852 / num_segments);
        final float p = (float)Math.cos(theta);
        final float s = (float)Math.sin(theta);
        float x;
        r = (x = r * 2.0f);
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawBorderedCircle(final int circleX, final int circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        drawCircle(circleX, circleY, (float)(radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, radius, innerColor);
        disableGL2D();
    }

    public static void drawCircleNew(final float x, final float y, final float radius, final int numberOfSides) {
        final float z = 0.0f;
        final int numberOfVertices = numberOfSides + 2;
        final float doublePi = 6.2831855f;
    }


    public static void drawFullCircle(int cx, int cy, double r, final int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 2160; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 360.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 360.0) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }







    public static void renderDonut(int x,int y,int donutwidth, int radius,int color)
    {
        for (int i = 1; i < 360; i++)
        {
            float rX1 = (float)Math.cos(Math.toRadians(i))   * donutwidth *- radius;
            float rY1 = (float)Math.sin(Math.toRadians(i) ) * donutwidth * - radius;
            float rX2 = (float)Math.cos(Math.toRadians(i) ) * donutwidth *+ radius;
            float rY2 = (float)Math.sin(Math.toRadians(i)) * donutwidth *+ radius;
            drawRect(x + rX1, y + rY1, x + rX2, y + rY2,color);
        }
    }

    public static void enableGL2D()
    {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D()
    {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }


    public static void disableGL3D()
    {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

}
