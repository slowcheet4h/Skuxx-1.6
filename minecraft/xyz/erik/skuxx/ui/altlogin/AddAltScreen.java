package xyz.erik.skuxx.ui.altlogin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.ui.buttons.SkuxxButton;

import java.io.IOException;

public class AddAltScreen
extends GuiScreen{

    public AddAltScreen() {
    }

    public void initGui() {
        addAlt = new SkuxxButton(1,width / 2 - 60,height / 2 + 60,120,20,"Add Alt");
        username = new GuiTextField(2, Helper.mc().fontRendererObj,width / 2 - 60,height / 2 - 50,120,20);
        email = new GuiTextField(2, Helper.mc().fontRendererObj,width / 2 - 60,height /  2 - 10,120,20);
        password = new GuiTextField(2, Helper.mc().fontRendererObj,width / 2 - 60,height / 2 + 30,120,20);
        this.buttonList.add(addAlt);
        super.initGui();
    }

    public SkuxxButton addAlt;
    public GuiTextField username;
    public GuiTextField email;
    public GuiTextField password;
    private String reason = null;
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        Helper.getVerdana().drawStringWithShadow("Username",width / 2 - 60,height / 2 - 62,-1);
        Helper.getVerdana().drawStringWithShadow("Email",width / 2 - 60,height / 2 - 22,-1);
        Helper.getVerdana().drawStringWithShadow("Password",width / 2 - 60,height / 2 + 18,-1);
        if (reason != null) {
            Helper.getVerdana().drawStringWithShadow(reason,width / 2 - Helper.getVerdana().getWidth(reason) / 2, height / 2 - 82,0xdd0000);
        }
        addAlt.drawButton(Helper.mc(),mouseX,mouseY);
        username.drawTextBox();
        email.drawTextBox();
        password.drawTextBox();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        email.mouseClicked(mouseX,mouseY,mouseButton);
        username.mouseClicked(mouseX,mouseY,mouseButton);
        password.mouseClicked(mouseX,mouseY,mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        email.textboxKeyTyped(typedChar,keyCode);
        username.textboxKeyTyped(typedChar,keyCode);
        password.textboxKeyTyped(typedChar,keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == 1) {
            if(username.getText() == null || username.getText().length() <= 1) {
                reason = "Username required";
                return;
            }

            if (password.getText().length() != 0 && email.getText().length() == 0) {
                reason = "Email Required";
                return;
            }

            AltLoginScreen.AltAccount alt = new AltLoginScreen.AltAccount(username.getText(),
                    email.getText()
                    ,password.getText(),
                    password.getText().length() > 0);
            AltLoginScreen.addAccountAndSave(alt);
            Helper.mc().displayGuiScreen(new AltLoginScreen());
        }
    }

    public boolean doesGuiPauseGame() {
        return true;
    }
}
