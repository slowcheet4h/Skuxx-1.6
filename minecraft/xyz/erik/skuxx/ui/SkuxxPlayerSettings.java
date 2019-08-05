package xyz.erik.skuxx.ui;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import xyz.erik.api.helpers.Helper;

import java.io.IOException;

public class SkuxxPlayerSettings
extends GuiScreen{

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Helper.getVerdana().drawStringWithShadow("WORK IN PROGRESS",width / 2 - Helper.getVerdana().getWidth("WORK IN PROGRESS") / 2,height / 2 - Helper.getVerdana().getHeight("WORK IN PROGRESS") / 2,-1);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
