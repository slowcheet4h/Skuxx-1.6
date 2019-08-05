package xyz.erik.skuxx;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.RenderUtil;

import java.awt.*;


public class RederpzButton
        extends GuiButton {
    public RederpzButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.displayString = buttonText;
    }

    public RederpzButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
        this.displayString = buttonText;
    }

    private float zoomTimer = 0.0F;

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height));

            int var5 = getHoverState(this.hovered);

            if (this.enabled) {
                if (this.hovered) {
                    if (this.zoomTimer > -1.0F) {
                        this.zoomTimer -= 0.35F;
                    }
                    RenderUtil.drawBorderedRect(this.xPosition + this.zoomTimer, this.yPosition + this.zoomTimer, this.xPosition + this.width - this.zoomTimer, this.yPosition + this.height - this.zoomTimer, 0.3F, -1148680056, -1148680056, false);
                } else {
                    this.zoomTimer = 0.0F;
                    RenderUtil.drawBorderedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0.3F, -1151969706, -1151969706, false);
                }
                this.drawString(this.displayString, (int) this.center(this.displayString, this.xPosition + this.width / 2 + 1.0F), (int) (this.yPosition + (this.height - 8) / 2 + 0.5F), this.hovered ? -1 : -3355444);
            } else {
                if (this.hovered) {
                    if (this.zoomTimer < 1.0F) {
                        this.zoomTimer += 0.35F;
                    }
                    RenderUtil.drawBorderedRect(this.xPosition + this.zoomTimer, this.yPosition + this.zoomTimer, this.xPosition + this.width - this.zoomTimer, this.yPosition + this.height - this.zoomTimer, 0.3F, -1153088187, -1153088187, false);
                } else {
                    this.zoomTimer = 0.0F;
                    RenderUtil.drawBorderedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0.3F, -1151969706, -1151969706, false);
                }
                this.drawString(this.displayString, (int) (this.center(this.displayString, (int) (this.xPosition + this.width / 2 + 1.0F))), (int) (this.yPosition + (this.height - 8) / 2 + 0.5F), -7829368);
            }
        }
    }

    private float center(String string, float x) {
        return x - Helper.getVerdana().getWidth(string) / 2;
    }


    public void drawString(String text, int x, int y, int color) {

        Helper.getVerdana().drawStringWithShadow(text, x, y, color);
        RenderUtil.setColor(Color.GRAY);
    }


    public void setText(String text) {
        this.displayString = text;
    }

    public void set(boolean enabled) {
        this.enabled = enabled;
    }
}