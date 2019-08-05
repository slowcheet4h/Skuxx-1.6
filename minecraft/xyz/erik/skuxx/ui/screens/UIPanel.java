package xyz.erik.skuxx.ui.screens;

import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class UIPanel {

    public int dragPosX;
    public int dragPosY;
    public int x;
    public int y;
    public List<Mod> modules = new ArrayList<>();
    public int outside_color = 0xff313131;
    public int inside_color = 0xff505050;
    public int loadedModules = 0;
    public int temp;
    public UIPanel(Category cat) {
        this.category = cat;
        modules = getModules();
    }

    public final Category category;
    int lastDragX = 0;
    int lastDragY = 0;

    int width = 90;
    public void drawPanel()
    {
        this.outside_color = 0xff212121;
        this.inside_color = 0xff303030;
        int pay = 4;
        RenderUtil.drawRect(x - pay,y,x + width + pay,y + 15 + loadedModules* 15 + 5,outside_color);

        Helper.getArrayFont().drawStringWithShadow(category.name,x + width / 2 - (Helper.getArrayFont().getWidth(category.name)
        /2),y + 2,-1);

        RenderUtil.drawBorderedRect(x,y+ 15,x + width,y + 15+ loadedModules* 15,2,-1,inside_color,true);


        int modY = y + 15;
        for (int i = 0; i < loadedModules; i++)
        {
            Mod mod = modules.get(i);
            RenderUtil.drawBorderedRect(x,modY,x + width,modY + 15,1,outside_color,mod.getState() ? -1 : 0xff252525,true);
            Helper.getSegaui().drawStringWithShadow(mod.getName(),x + 1,modY,-1);
            modY+=15;
        }
        temp++;
        if (loadedModules < modules.size() && temp >= 1) {
            temp = 0;
            loadedModules++;
        }


    }



    public boolean isOverModules(int mouseX,int mouseY)
    {
        boolean check1 = (mouseX >= x && mouseX <= x + width);
        boolean check2 = (mouseY >= y + 15 && mouseY <= y + 15 + loadedModules * 15);

        return check1 && check2;


    }

    public void clickModule(int mouseY) {
        mouseY -= 5;
        double closest = 51227;
        Mod mod = null;
        for (int i = 0; i < loadedModules; i++)
        {
            int mY = y + 15 + i * 15;
            int dist = Math.abs(mY - mouseY);

            System.out.println(mouseY + "  " + mY + " dist " + dist);
            if (dist < closest)
            {
                mod = modules.get(i);
                closest = dist;
            }

        }

        System.out.println(closest);

        if (mod != null)
        {
            mod.toggle();
        }
    }
    public boolean isMouseInside(int mouseX,int mouseY)
    {
        boolean inside1 = (mouseX >= x - 4 && mouseX <= x) || (mouseX >= x + width && mouseX <= x + width + 4)
                || (mouseY >= y && mouseY <= y + 15);
        boolean insideY = mouseY >= y && mouseY <= modules.size() * 15 + 15 + y;
        return inside1 && insideY;
    }

    public void click(int mouseX, int mouseY)
    {

        this.dragPosX = mouseX;
        this.dragPosY = mouseY;
    }
    public void update(int mouseX,int mouseY) {


        if(!released) {
            this.x = mouseX;
            this.y = mouseY;

        }

    }


    public boolean released = true;

    public List<Mod>
    getModules() {
        List<Mod> mods = new ArrayList<>();
        for (
                Mod mod :
                Skuxx.getInstance().getModManager().getMods()
                ) {

            if (mod.getCategory() == category)
            {
                mods.add(mod);
            }

        }

        return mods;
    }

}
