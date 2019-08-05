package xyz.erik.skuxx.ui.altlogin;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.alt.AltManager;
import xyz.erik.api.alt.Login;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.FileUtils;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.ui.buttons.SkuxxButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AltLoginScreen extends GuiScreen{

    private File file;
    public static List<AltAccount> altAccounts = new ArrayList<>();
    public int focusedY;
    public int selectedIndex;


    public SkuxxButton addAltButton;
    public AltLoginScreen() {
        altAccounts.clear();
        file = FileUtils.getConfigFile("alts");
        load();
        save();

    }
    //do later
    public void save() {
        List<String> accs = new ArrayList<>();
        for(AltAccount e : altAccounts) {
            accs.add(e.username + "::" + e.email + "::" + e.password + "::" + e.isPremium);
        }
        FileUtils.write(file,accs,true);
    }
    //do later
    public void load() {
        List<String> str = FileUtils.read(file);
        for(String s : str) {
            String split[] = s.split("::");
                AltAccount a = new AltAccount(split[0],split[1],split[2],Boolean.parseBoolean(split[3]));
            altAccounts.add(a);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_A) {
            Helper.mc().displayGuiScreen(new AddAltScreen());
        }
        if (keyCode == Keyboard.KEY_D) {
            Helper.mc().displayGuiScreen(new AltManager(this));
        }
        super.keyTyped(typedChar,keyCode);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int y = 25 - focusedY;
        int x = 35;
        Helper.getVerdana().drawStringWithShadow("Username: " + Minecraft.getMinecraft().session.getUsername(),2,2,-1);
        for(int i = 0; i < altAccounts.size(); i++) {
            AltAccount altAccount = (altAccounts.get(i));
            boolean isOver = mouseX > x - 15 && mouseX < x + 15 && mouseY > y + 20 - 15 && mouseY < y + 20 + 15;
            RenderUtil.drawFullCircle(x, y + 20,30,isOver ? 0xff606060 : 0xff202020);
            Helper.getVerdana().drawStringWithShadow(altAccount.username,x  - Helper.getVerdana().getWidth(altAccount.username) / 2, y + 10,-1);
            Helper.getSmallFont().drawStringWithShadow(altAccount.isPremium ? "Premium" : "Non-Premium",x - Helper.getSmallFont().getWidth(altAccount.isPremium ? "Premium" : "Non-Premium") / 2,y + 20,altAccount.isPremium ? 0x0055ff: 0xdd0000);
            y += 65;
            if(y > height - 10) {
                y = 25 - focusedY;
                x += 65;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public boolean doesGuiPauseGame() {
        return true;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int y = 25 - focusedY;
        int x = 35;
        for(int i = 0; i < altAccounts.size(); i++) {
            AltAccount altAccount = (altAccounts.get(i));
            boolean isOver = mouseX > x - 15 && mouseX < x + 15 && mouseY > y + 20 - 15 && mouseY < y + 20 + 15;
            if (isOver) {
                System.out.println(altAccount.username);
                if (altAccount.isPremium) {
                    final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                    final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
                    auth.setUsername(altAccount.email);

                    auth.setPassword(altAccount.password);
                    Session session;
                    try {
                        auth.logIn();
                        session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
                    }
                    catch (AuthenticationException e) {
                         session = null;
                    }
                    if (session==null) {
                        System.out.println("ERROR");
                        return;
                    }
                    Minecraft.getMinecraft().session = session;
                } else {
                    this.mc.session = new Session(altAccount.username, "", "", "mojang");
                }

            }
            y += 65;
            if(y > height - 10) {
                y = 25 - focusedY;
                x += 65;
            }
        }
        super.mouseClicked(mouseX,mouseY,mouseButton);
    }

    protected void actionPerformed(GuiButton button) throws IOException {

    }

    public static void addAccountAndSave(AltAccount altAccount) {
        List<String> halihazirda = FileUtils.read(FileUtils.getConfigFile("alts"));
        halihazirda.add(altAccount.username + "::" + altAccount.email + "::" + altAccount.password + "::" + altAccount.isPremium);
        FileUtils.write(FileUtils.getConfigFile("alts"),halihazirda,true);
    }

    public static class AltAccount {
        public String username;
        public String email;
        public boolean isPremium;
        public String password;
        public AltAccount(String username, String password, String email, boolean isPremium) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.isPremium = isPremium;
        }

    }
}
