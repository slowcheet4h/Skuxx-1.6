package xyz.erik.api.alt;

/**
 * Created by Karanlik on 6/19/2017.
 */



import net.minecraft.client.gui.GuiScreen;


import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.RederpzButton;

public final class AltManager
        extends GuiScreen
{
    private Alt password;
    private final GuiScreen previousScreen;
    private Login thread;
    private GuiTextField username;
    private GuiTextField both;
    public static final ResourceLocation BG1 = new ResourceLocation("textures/BackGrounds/Background.jpg");

    public AltManager(GuiScreen previousScreen)
    {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 1:
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            case 0:
                if (both.getText().length() > 5 && both.getText().contains(":")) {
                    String[] Usernameandpassword = both.getText().split(":");
                    this.thread = new Login(Usernameandpassword[0], Usernameandpassword[1]);
                    this.thread.start();
                } else {
                    this.thread = new Login(this.username.getText(), this.password.getText());
                    this.thread.start();
                }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.both.drawTextBox();
        drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 78,
                -1);
        drawCenteredString(this.mc.fontRendererObj, this.thread == null ? "§6Waiting..." :
                this.thread.getStatus(), this.width / 2, 88, -1);
        if (this.username.getText().isEmpty()) {
            drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96,
                    106, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 136,
                    -7829368);
        }
        if (this.both.getText().isEmpty()) {
            drawString(this.mc.fontRendererObj, "Username:Password", this.width / 2 - 96, 166,
                    -7829368);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui()
    {
        int var3 = this.height / 4 + 24;
        this.buttonList.add(new RederpzButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new RederpzButton   (1, this.width / 2 - 100, var3 + 72 + 12 + 24,
                "Exit"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 100, 200,
                20);
        this.password = new Alt(this.mc.fontRendererObj, this.width / 2 - 100, 130,
                200, 20);
        this.both = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 160, 200,
                20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    public void renderBG(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(BG1);
        Tessellator var3 = Tessellator.getInstance();
        var3.getWorldRenderer().startDrawingQuads();
        var3.getWorldRenderer().addVertexWithUV(0.0D, (double)par2, -90.0D, 0.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV((double)par1, (double)par2, -90.0D, 1.0D, 1.0D);
        var3.getWorldRenderer().addVertexWithUV((double)par1, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    protected void keyTyped(char character, int key)
    {
        try
        {
            super.keyTyped(character, key);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (character == '\t') {
            if ((!this.username.isFocused()) && (!this.password.isFocused()) && (!this.both.isFocused()))
            {
                this.username.setFocused(true);
            }
            else
            {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
         if (character == '\r') {
        actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
        this.both.textboxKeyTyped(character, key);
    }

    protected void mouseClicked(int x, int y, int button)
    {
        try
        {
            super.mouseClicked(x, y, button);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
        this.both.mouseClicked(x, y, button);
    }

      public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen()
    {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        this.both.updateCursorCounter();
    }
}