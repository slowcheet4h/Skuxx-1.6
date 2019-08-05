package xyz.erik.skuxx.ui.click;

import net.minecraft.client.gui.GuiScreen;
import xyz.erik.skuxx.Skuxx;

import java.io.IOException;

public class ClickConsole
extends GuiScreen{


    public ClickConsole() {
        if (Skuxx.console == null) {
            Skuxx.console = new Console();
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Skuxx.console.render(width,height);
    }


    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Skuxx.console.mouseReleased(mouseX,mouseY);
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Skuxx.console.mousePressed(mouseX,mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Skuxx.console.keyTyped(keyCode);
    }



    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Skuxx.console.mouseDragged(mouseX,mouseY);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public boolean doesGuiPauseGame() {
        return Skuxx.getInstance().getModManager().getMod("Inventory").getState();
    }
}
