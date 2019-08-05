package xyz.erik.skuxx.ui.irc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.jibble.pircbot.User;
import xyz.erik.api.font.FontUtil;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.helpers.Helper;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.irc.Myself;

public class IRCScreen
        extends GuiScreen
{
    private List<String> onlineUsers;
    private List<String> chatMessages = new ArrayList();
    private String lastThingTyped = "";
    private ErikTimer erikTimer;
    boolean doNext;

    public IRCScreen()
    {
        this.onlineUsers = new ArrayList();
        this.fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        this.erikTimer = new ErikTimer();
    }

    public void updateOnlineUsers()
    {
        this.onlineUsers.clear();
        for (User s : Skuxx.getInstance().getMyself().getUsers("#skuxx0")) {
            this.onlineUsers.add(s.getNick().replace("skx", ""));
        }
    }

    public boolean doesGuiPauseGame()
    {
        return true;
    }

    public void onGuiClosed()
    {
        this.lastThingTyped = "";
    }

    public List<String> getMessages()
    {
        return this.chatMessages;
    }

    protected void keyTyped(char typedChar, int keyCode)
            throws IOException
    {
        if (keyCode == 28)
        {
            sendMessage(this.lastThingTyped);
            this.lastThingTyped = "";
        }
        else if (keyCode == 14)
        {
            if (this.lastThingTyped.length() > 0) {
                this.lastThingTyped = this.lastThingTyped.substring(0, this.lastThingTyped.length() - 1);
            }
        }
        else
        {
            String typeAbleChars = "@qwabcdefg�h�ijklmno�prs�tu�vyzx:0123456789();%+'^{}*-_<>�?$ !?*/.,#+�[]�";
            if (typeAbleChars.contains((typedChar + "").toLowerCase())) {
                this.lastThingTyped += typedChar;
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    public void sendMessage(String message)
    {
        if (message.startsWith(" ")) {
            return;
        }
        if (message.startsWith("@!gaysarki")) {
            return;
        }
        addChatMessage(Skuxx.getUser(), message);
        Skuxx.getInstance().getMyself().sendMessage(message);
    }

    public void updateScreen()
    {
        if (this.erikTimer.delay(500.0F))
        {
            this.doNext = (!this.doNext);
            this.erikTimer.reset();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawGradientRect(0, 0, this.width, this.height, -1728053248, -16777216);
        Helper.getArrayFont().drawStringWithShadow("Online (" + this.onlineUsers.size() + ")", this.width - Helper.getArrayFont().getWidth("Online (" + this.onlineUsers.size() + ")") - 4.0F, 2.0F, -1);
        int ys = 15;
        for (String s : this.onlineUsers)
        {
           Helper.getArrayFont().drawStringWithShadow("@" + s, this.width - 12 - Helper.getArrayFont().getWidth(s), ys, s.replace("skx","").replace("-rl","").equalsIgnoreCase(Skuxx.getUser()) ? -6301338 : -1);
            ys += 10;
        }
        int y = this.height - 25;
        for (int i = this.chatMessages.size() - 1; i > 0; i--)
        {
            int color = ((String)this.chatMessages.get(i)).split(":")[0].contains(Skuxx.getUser()) ? -6301338 : 3158064;
            String str = ((String)this.chatMessages.get(i)).split(":")[1];
            Helper.getArrayFont().drawStringWithShadow(str, 2.0F + Helper.getArrayFont().getWidth(((String)this.chatMessages.get(i)).split(":")[0]) + 1.0F, y, -1);

            Helper.getArrayFont().drawStringWithShadow(((String)this.chatMessages.get(i)).split(":")[0] + ":", 2.0F, y, color);
            y -= 10;
        }
        Helper.getArrayFont().drawStringWithShadow(this.lastThingTyped + (this.doNext ? " >" : ""), 2.0F, this.height - 12, this.lastThingTyped.startsWith("@!") ? -6631557 : -1);
    }

    public void addChatMessage(String sender, String message)
    {
        if (message.startsWith("@!")) {
            return;
        }
        this.chatMessages.add("[" + sender.replace("skx", "") + "]: " + message);
        if ((0 > this.height - this.chatMessages.size() * 10) && (this.chatMessages.size() != 1)) {
            this.chatMessages.remove(0);
        }
    }
}
