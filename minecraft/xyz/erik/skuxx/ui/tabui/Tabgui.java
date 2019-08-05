package xyz.erik.skuxx.ui.tabui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Tabgui {


    public Tabgui() {
        startY = 5;
    }


    public int catIndex;
    public Category selCategory;
    public int modIndex;
    public Mod selMod;
    public boolean isTabbed;

    public int startY;

    public void show() {
        int x = 8+ 40;
        int y = 2;
        for(int i = 0; i < Category.values().length; i++) {
            Category cat = (Category)Category.values()[i];
            if(!isTabbed || (i == catIndex)) {
                RenderUtil.drawRoundedRect(x, y, x + (isTabbed ? 60 : 20), y + 7, 0xff404040, catIndex == i ? 0xff9d13c9 : 0xff202020);
                Helper.getSmallFont().drawStringWithShadow( isTabbed ? cat.name : cat.name.charAt(0) + "", x + (isTabbed ? 60 - Helper.getSmallFont().getWidth(cat.name) : 12), y - 2, -1);
                if(isTabbed) {
                    int yMod = y + 9;
                    for(int j = 0; j < getMods(cat).size(); j++) {
                        Mod mod = getMods(cat).get(j);
                        RenderUtil.drawRoundedRect(x,yMod,x + 60, yMod + 9,0xff404040, j == modIndex ? 0xff9d13c9 : 0xff202020);
                        Helper.getSmallFont().drawStringWithShadow(mod.getName(),x + 1,yMod, mod.getState() ? -3355444 : -1);
                        yMod+= 9;
                    }
                }
            }
            x-= 4;
            y += 8;
        }

        RenderUtil.drawFullCircle(10,10,40,0xff202020);
    }

    public void keyDown(int key) {
        switch(key) {
            case Keyboard.KEY_DOWN:
                if(isTabbed) {
                    modIndex++;
                    if(modIndex > getMods(selCategory).size() - 1) {
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
                if(isTabbed) {
                    modIndex--;
                    if(modIndex < 0) {
                        modIndex = getMods(selCategory).size() -1;
                    }
                } else {
                    catIndex--;
                    if (catIndex < 0) {
                        catIndex = Category.values().length - 1;
                    }
                }

                break;
            case Keyboard.KEY_RIGHT:
                if (isTabbed) {
                    String name = (getMods(Category.values()[catIndex]).get(modIndex).getName());
                    Skuxx.getInstance().getModManager().getMod(name).toggle();
                    break;
                }
                isTabbed = true;
                selCategory = Category.values()[catIndex];
                break;
            case Keyboard.KEY_LEFT:
                isTabbed = false;
                break;
            case Keyboard.KEY_RETURN:


                break;
        }
    }

    public List<Mod> getMods(Category cat) {
        List<Mod> mods = new ArrayList<>();
        for(Mod mod : Skuxx.getInstance().getModManager().getMods()) {
            if(mod.getCategory() == cat) {
                mods.add(mod);
            }
        }

        mods.sort(new Comparator<Mod>() {
            @Override
            public int compare(Mod o1, Mod o2) {

                return (int) o1.getName().charAt(0) - (int)o2.getName().charAt(0);
            }
        });

        return mods;
    }




}
