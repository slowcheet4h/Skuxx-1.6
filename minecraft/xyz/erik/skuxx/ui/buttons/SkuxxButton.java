package xyz.erik.skuxx.ui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;

public class SkuxxButton
extends GuiButton{

    public SkuxxButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId,x,y,widthIn,heightIn,buttonText);
    }

    int color;
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        boolean isOver = mouseX > xPosition && mouseX < xPosition + width && mouseY > yPosition && mouseY < yPosition + height;
        RenderUtil.drawRoundedRect(xPosition,yPosition,xPosition + getButtonWidth(), yPosition + this.height,0xff202020,isOver ?  0xff606060 : 0xff404040);
        Helper.getVerdana().drawStringWithShadow(displayString,xPosition + width / 2 - Helper.getVerdana().getWidth(displayString) / 2, yPosition + height / 2 - 5,-1);
    }

    @Override
    public boolean isMouseOver() {
        return super.isMouseOver();
    }


}
