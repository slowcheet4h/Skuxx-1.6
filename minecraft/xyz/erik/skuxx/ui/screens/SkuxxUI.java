package xyz.erik.skuxx.ui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.mods.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class    SkuxxUI
        extends GuiScreen {

    private int deltaX;
    private int deltaY;
    private int guiWidth;
    private int guiHeight;
    private Category selectedCategory;
    private Mod selectedMod;

    public SkuxxUI() {
        guiWidth = 400;
        guiHeight = guiWidth / 12 * 7;
        deltaX = 0;
        deltaY = 0;
    }




    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x1 = width / 2 - (deltaX / 2);
        int x2 = width / 2 + (deltaX / 2);
        int y1 = height / 2 - (deltaY / 2);
        int y2 = height / 2 + (deltaY / 2);

        if (deltaX < guiWidth) deltaX+=3;
        if (deltaY < guiHeight) deltaY+=3;

        RenderUtil.drawRect(x1,y1,x2,y2,0xff353839);
        Helper.getCourerer().drawStringWithShadow("SkuxxUI",width / 2 - (Helper.getCourerer().getWidth("SkuxxUI") / 2),
                y1 + 2,-1);
        // PUT DRAWRECT HERE
        boolean isOverClose = mouseX >= x2 - 15 && mouseX <= x2 - 2 && mouseY >= y1 + 2 && mouseY <= y1+ 12;
        RenderUtil.drawRect(x2 - 15,y1 + 2,x2 - 2,y1 + 12,isOverClose ? 0xffFF4545 : 0x99FF4545);
        Helper.getCourerer().drawStringWithShadow("x",x2 - 12,y1 + 1,0xB8B8B8   );

        int x = x1 + 20;
        int yCat = height / 2 - (guiHeight / 2) + 18;
        for(int cIn = 0; cIn < Category.values().length; cIn++) {
            Category cat = Category.values()[cIn];
            if (x >= x2 - Helper.getCourerer().getWidth(cat.name) + 2 || yCat < y1) break;
            boolean isOverCat = mouseX >= x && mouseX <= x + Helper.getCourerer().getWidth(cat.name) && mouseY >=yCat && mouseY <= yCat + 5;
            if (isOverCat || selectedCategory == cat)
                RenderUtil.drawRect(x - 2,yCat - 2,x + Helper.getCourerer().getWidth(cat.name) + 2,yCat + 10,selectedCategory != cat ? 0xff303030 : 0xff404040);
            Helper.getCourerer().drawStringWithShadow(cat.name,x,yCat,isOverCat ?  -1 : 0xB8B8B8);
            if (cat == selectedCategory) {
                RenderUtil.drawRect(x1 + 10,yCat + 10,x2 - 10,y2 - 10,0xff404040);
            }
            x += Helper.getCourerer().getWidth(cat.name) + 40;

            //put drawrect




        }

        if (selectedCategory != null) {
            int modY = y1 + 30;
            int modX = x1 + 13;
            for (int i = 0; i < getModulesForCategory(selectedCategory).size(); i++)
            {
                Mod mod = getModulesForCategory(selectedCategory).get(i);
                boolean isOverMod = mouseX >= modX && mouseX <= modX + Helper.getCourerer().getWidth(mod.getName())
                        && mouseY >= modY && mouseY <= modY + 5;

                RenderUtil.drawBorderedRect(modX - 2,modY - 2,modX + Helper.getCourererSmall().getWidth(mod.getName()
                ) + 2,modY + 12,0.6F,0xff404040, isOverMod || selectedMod == mod ?0xff404040 :  0xff303030 ,true);
                Helper.getCourererSmall().drawStringWithShadow(mod.getName(),modX, modY,isOverMod ? - 1 : 0xB8B8B8);


                modX += Helper.getCourererSmall().getWidth(mod.getName()) + 6;
                if (modX + Helper.getCourererSmall().getWidth(mod.getName()) + 6 >= x2 - 10) {
                    modX = x1 + 13;
                    modY += 13;
                }

                if(selectedMod == mod)
                {
                    Helper.getSegaui().drawStringWithShadow(mod.getName(),
                            width / 2   - (Helper.getSegaui().getWidth(mod.getName())) / 2 ,guiHeight / 2 + 30,
                            -1);

                    boolean isOverToggle = mouseX >= width / 2 - 20 && mouseX <= width / 2 + 20 && mouseY >= guiHeight / 2 + 45 && mouseY <= guiHeight / 2 + 55;

                    RenderUtil.drawRect(width / 2 - 20,guiHeight / 2 + 45,width / 2 + 20,guiHeight / 2 + 55,isOverToggle ? 0xff707070: 0xff303030);

                    Helper.getCourererSmall().drawStringWithShadow("Toggle",width / 2 -
                                    Helper.getCourererSmall().getWidth("Toggle") / 2,
                            guiHeight / 2 + 45,mod.getState() ? - 1 : 0xDFDFDF);




                    if(mod.haveSettings())
                    {

                        for (Bool bool : mod.getBoolSets()) {

                        }


                    }
                }
            }
        }
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public void updateScreen() {

    }



    public List<Mod> getModulesForCategory(Category cat) {
        List<Mod> mods = new ArrayList<>();

        for (Mod mod : Skuxx.getInstance().getModManager().getMods())
        {
            if (mod.getCategory() == cat) mods.add(mod);
        }

        return mods;


    }



    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {

        int x1 = width / 2 - (deltaX / 2);
        int x2 = width / 2 + (deltaX / 2);
        int y1 = height / 2 - (deltaY / 2);
        int y2 = height / 2 + (deltaY / 2);

        boolean isOverClose = mouseX >= x2 - 15 && mouseX <= x2 - 2 && mouseY >= y1 + 2 && mouseY <= y1+ 12;
        if (isOverClose) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }

        int x = x1 + 20;
        int yCat = y1 + 18;
        for(int cIn = 0; cIn < Category.values().length; cIn++) {
            Category cat = Category.values()[cIn];

            boolean isOverCat = mouseX >= x && mouseX <= x + Helper.getCourerer().getWidth(cat.name) && mouseY >= yCat && mouseY <= yCat + 5;
            if (isOverCat)
            {
                selectedCategory = cat;
                break;
            }
            x += Helper.getCourerer().getWidth(cat.name) + 40;

        }
        int modY = y1 + 30;
        int modX = x1 + 13;
        for (int i = 0; i < getModulesForCategory(selectedCategory).size(); i++) {
            Mod mod = getModulesForCategory(selectedCategory).get(i);
            boolean isOverMod = mouseX >= modX && mouseX <= modX + Helper.getCourerer().getWidth(mod.getName())
                    && mouseY >= modY && mouseY <= modY + 5;
            if (isOverMod) {
                selectedMod = mod;
                break;
            }
            modX += Helper.getCourererSmall().getWidth(mod.getName()) + 6;
            if (modX + Helper.getCourererSmall().getWidth(mod.getName()) + 6 >= x2 - 10) {
                modX = x1 + 13;
                modY += 13;
            }
        }
        if (selectedMod != null) {

            boolean isOverToggle = mouseX >= width / 2 - 20 && mouseX <= width / 2 + 20 && mouseY >= guiHeight / 2 + 45 && mouseY <= guiHeight / 2 + 55;
            if (isOverToggle) selectedMod.toggle();
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }
}
/*
    private int guiWidth;
    private int guiHeight;
    private Category selectedCategory;
    private Mod selectedMod;
    public List<UIPanel> panels = new ArrayList<>();
    public SkuxxUI() {
        if (panels.size() == 0) {
            int x = 100;
            for (Category cat : Category.values()) {
                UIPanel panel = new UIPanel(cat);
                panel.x = x;
                panel.y = 70;
                x += 110;
                panels.add(panel);

            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.
                drawGradientRect(0,0,width,height,0xff000000,0x70000000);
        for (UIPanel p : panels)
        {
            p.update(mouseX,mouseY);
        }

        for (UIPanel panel : panels)
        {
            panel.drawPanel();
        }







    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }




    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

         }



    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (UIPanel ui : panels)
        {
            ui.released = true;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (UIPanel ui : panels)
        {
            if (ui.isMouseInside(mouseX,mouseY))
            {

                ui.click(mouseX,mouseY);
                ui.released = false;
                break;
            }

            if (ui.isOverModules(mouseX,mouseY))
            {
                ui.clickModule(mouseY);
                System.out.println("YOU CLICKED SOMETHING");
            }
        }

    }
}
*/
