package xyz.erik.api.tabgui;

import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.font.FontUtil;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventRender;
import xyz.erik.skuxx.event.events.KeyPressEvent;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.List;

public class PixelTabgui {

    private int BACKGROUND_COLOR;
    private int OVER_COLOR;
    private int OVERLAY_COLOR;

    private int catIndex,modIndex;
    private boolean isTabbed;
    public PixelTabgui() {
        this.BACKGROUND_COLOR = 0x90232b2b;
        this.OVER_COLOR = 0xFF4B0082;
        this.OVERLAY_COLOR = 0xFF323232;

    }


    public void onRender(int height, int width) {
        int y = 20;
        int tempY = y;

            if (Helper.mc().gameSettings.showDebugInfo || Helper.mc().currentScreen instanceof GuiChat) return;
            for(int ind = 0; ind < Category.values().length; ind++) {
                Category category = Category.values()[ind];
                RenderUtil.rect(2,y,50,y + 11,catIndex == ind? 0xFF4B0082 : BACKGROUND_COLOR);
                Helper.getMinecraftFont().drawStringWithShadow(category.name,5,y, -1);

                int modY = y;
            y += 11;
            if (isTabbed && ind == catIndex) {
                for (int index = 0; index < getModsFromCategory(category).size(); index++) {
                    Mod mod = getModsFromCategory(category).get(index);

                    RenderUtil.rect(52,modY, 52 +getLongestModLenght(category,Helper.getComfortaFont()) * 3 - 2,modY + 10, modIndex == index ? 0xFF4B0082 :  BACKGROUND_COLOR);
                    Helper.getMinecraftFont().drawStringWithShadow(mod.getName(),52,modY,mod.getState() ? -3355444 : -1);
                    modY += 10;
                }
                RenderUtil.drawBorderedRect(52,y - 11,52+getLongestModLenght(category,Helper.getComfortaFont()) * 3 - 2,modY,1,OVERLAY_COLOR,0,true);
            }

        }
        tempY = Category.values().length * 11 + 20;
        RenderUtil.drawBorderedRect(2,20,50,tempY,1,OVERLAY_COLOR,0,true);

    }




    public List<Mod> getModsFromCategory(Category cat) {
        List<Mod> mods = new ArrayList<>();
        for (Mod mod : Skuxx.getInstance().getModManager().getMods()) {
            if (mod.getCategory() == cat) {
                mods.add(mod);
            }
        }
        return mods;
    }


    public void onKey(KeyPressEvent pressEvent) {
        int key = pressEvent.getKey();
        BACKGROUND_COLOR = 0xFF232b2b;
        switch (key) {

            case Keyboard.KEY_DOWN:

                if (isTabbed) {
                    modIndex++;
                    if (modIndex > getModsFromCategory(Category.values()[catIndex]).size()) {
                        modIndex = 0;
                    }
                } else {
                    catIndex++;
                    if (catIndex >= Category.values().length) {
                        catIndex = 0;
                    }
                }

                break;
            case Keyboard.KEY_UP:

                if (isTabbed) {
                    modIndex--;
                    if (modIndex > getModsFromCategory(Category.values()[catIndex]).size()) {
                        modIndex = 0;
                    }
                } else {
                    catIndex--;
                    if (catIndex < 0) {
                        catIndex = Category.values().length - 1;
                    }
                }

                break;
            case Keyboard.KEY_RIGHT:
                modIndex = 0;
                isTabbed = true;


                break;
            case Keyboard.KEY_LEFT:
                isTabbed = false;
                break;
            case Keyboard.KEY_RETURN:
                (getModsFromCategory(Category.values()[catIndex]).get(modIndex)).toggle();
                break;

                //TODO: ADD SETTINGS TO TABGUI
        }
    }



    private float getLongestModLenght(Category category, FontUtil font) {
        float longestLenght = 0;
        for (Mod m : getModsFromCategory(category)) {
            if (m.getCategory() == category) {
                float nowLenght = font.getWidth(m.getName());
                if (nowLenght > longestLenght) {
                    longestLenght = nowLenght;
                }
            }

        }
        return 20;
    }




}
