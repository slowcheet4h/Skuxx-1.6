package xyz.erik.skuxx.irc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.api.player.Player;
import xyz.erik.api.utils.TrayIcon;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.irc.irccmd.CapePlayer;
import xyz.erik.skuxx.irc.irccmd.IRCCommand;
import xyz.erik.skuxx.irc.irccmd.IRCCommands;
import xyz.erik.skuxx.mods.exploits.IRC;
import xyz.erik.skuxx.splayer.SkuxxPlayer;
import xyz.erik.skuxx.ui.irc.IRCScreen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Myself
extends PircBot
{


    public Myself() {
        this.setName(Skuxx.getUser() + "-rl");
    }


    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {

        if(message.startsWith("&")) {
            message = null;
            return;
        }
        if (message.startsWith("--")) {
            execute(message.replace("--", ""));
        }
        if (message.startsWith("@!")) {
            IRCCommand cmd = IRCCommands.getCommand(message);
            if (cmd != null)
                cmd.run(message);

            return;
        }


            Minecraft.getMinecraft().ircScreen.addChatMessage(sender, message);
        ((IRC)  Skuxx.getInstance().getModManager().getMod("IRC")).getMessage(sender,message);
    }

    protected void onJoin(String channel, String sender, String login, String hostname) {
        System.out.println("Logged " + sender);
        SkuxxPlayer pl = new SkuxxPlayer();
        pl.setName(sender.replace("skx",""));
        Skuxx.skuxxPlayerList.add(pl);
        super.onJoin(channel, sender, login, hostname);
    }

    public void sendMessage(String message) {
        super.sendMessage("#skuxx772",message);
    }

    public void sendDataMessage(String message) {
        message = "##" + message;
        this.sendMessage(message);
    }



    public void execute(String text) {

    }

    private String getComputerName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

    @Override
    public String getNick() {
        return super.getNick();
    }


    /**
     * Gives a HWID I.E (350-30a-3ae-30e-304-3d6-37d-359-371-3e0-3d8-3e1-369-3b2-34a-314) - Hexeption
     * @return
     * @throws NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }
}
