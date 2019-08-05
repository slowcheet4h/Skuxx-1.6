package xyz.erik.skuxx.ui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.alt.Login;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.FileUtils;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.RederpzButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkuxxAltUI
extends GuiScreen{

    int index;
    public Minecraft mc;
    public Alt selectedAlt;
    public GuiScreen previusScreen;
    RederpzButton button;
    public File file;
    private List<Alt> altAccounts = new ArrayList<>();


    public SkuxxAltUI(Minecraft mc,GuiScreen previusScreen) {
        this.mc = Minecraft.getMinecraft();
        this.previusScreen = previusScreen;
        file = FileUtils.getConfigFile("alts");
        load();
        save();
        if (altAccounts.size() !=0 && selectedAlt == null) {
            selectedAlt = altAccounts.get(0);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        RenderUtil.drawRect(0,0,width,40,0xff505050);
        RenderUtil.drawRect(0,height - 40,width,height,0xff505050);
        if (logged)
        Helper.getSkuxxFont().drawStringWithShadow("Logged with " + account,width / 2 - Helper.getSkuxxFont().getStringWidth("Logged with " + account) / 2,20,-6301338,0,0.5F);
        int y = 45;

        for (int i = index; i < altAccounts.size(); i++)
        {
            if (y  + 45> height - 40) break;
            Alt alt = altAccounts.get(i);
            int x1 = width / 2 - 80;
            int x2 = width / 2 + 80;
            if (selectedAlt.username == alt.username)
            RenderUtil.drawBorderedRect(x1,y,x2,y + 40,1,-1,0,true);


            Helper.getConsolasFont().drawStringWithShadow(alt.username,width / 2 - Helper.getConsolasFont().getStringWidth(alt.username) / 2,y + 5,-3355444,0,0.5F);

            if (alt.isPremium) {
                Helper.getConsolasFont().drawStringWithShadow("Premium",width / 2 - Helper.getConsolasFont().getStringWidth("Premium") / 2,y + 15,-6301338,0,0.5F);
                String p = "";
                for (int a = 0; a < alt.password.length(); a++) {
                    p += "*";
                }

                Helper.getConsolasFont().drawStringWithShadow(p,width/ 2 - Helper.getConsolasFont().getStringWidth(p) / 2,y + 30,0x707070,0,0.5F);
            } else {
                Helper.getConsolasFont().drawStringWithShadow("Cracked",width / 2 - Helper.getConsolasFont().getStringWidth("Cracked") / 2,y + 15,0x606060,0,0.5F);

            }
            y += 45;

        }

    }



    public void load() {
        altAccounts.clear();
        List<String> strings = FileUtils.read(file);
        for (String string : strings)
        {
            String username = string.split("::")[0];
            String password = string.split("::")[1];
            boolean premium = string.split("::")[2].equalsIgnoreCase("yes");

            Alt alt = new Alt(username,password,premium);
            altAccounts.add(alt);
        }

    }

    public void save() {
        List<String> save = new ArrayList<>();
        for(Alt alt : altAccounts)
        {
            save.add(alt.username + "::" + alt.password + "::" + (alt.isPremium == true ? "yes" : "no"));
        }
        FileUtils.write(file,save,true);
    }



    public void updateScreen() {

    }

    protected void actionPerformed(GuiButton button) throws IOException {

    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {

        int y = 45;
        Alt selected = null;
        for (int i = index; i < altAccounts.size(); i++)
        {
            Alt alt = altAccounts.get(i);
            if (mouseY >= y && mouseY <= y + 40) {
                selected = alt;
            }
            y += 45;
        }
        if (selected != null)
            selectedAlt = selected;

    }
    public boolean logged;


    public Thread thread;
    public String account;
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode)
        {
            case Keyboard.KEY_DOWN:
            {
                index++;
                if (index >= altAccounts.size()) {
                    index = 0;
                }
                selectedAlt = altAccounts.get(index);
            break;
            }

            case Keyboard.KEY_UP: {
                index--;
                if (index < 0) index = altAccounts.size() -1;
                selectedAlt = altAccounts.get(index);
                break;
            }
            case Keyboard.KEY_RETURN:
            {
                if (selectedAlt != null) {
                    logged = true;
                    this.thread = new Login(this.selectedAlt.username, selectedAlt.password);
                    this.thread.start();
                    account = Minecraft.getMinecraft().session.getUsername();
                }
            }
                break;
        }
    }

    public class Alt {

        public String username;
        public String password;
        public boolean isPremium;


        public Alt(String username, String password, boolean isPremium)
        {
            this.isPremium = isPremium;
            this.password = password;
            this.username = username;
        }


    }

}
