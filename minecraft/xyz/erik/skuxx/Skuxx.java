package xyz.erik.skuxx;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import javazoom.jl.decoder.JavaLayerException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.util.Session;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.User;
import org.lwjgl.opengl.Display;
import xyz.erik.api.command.CommandManager;
import xyz.erik.api.config.ConfigManager;
import xyz.erik.api.friend.FriendManager;
import xyz.erik.api.mod.ModManager;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.api.utils.NetworkUtils;
import xyz.erik.skuxx.cfg.SkuxxConfig;
import xyz.erik.skuxx.irc.Myself;
import xyz.erik.skuxx.irc.irccmd.IRCCommand;
import xyz.erik.skuxx.irc.irccmd.IRCCommands;
import xyz.erik.skuxx.mods.untoggleable.Cmd;
import xyz.erik.skuxx.mods.untoggleable.Render;
import xyz.erik.skuxx.splayer.SkuxxPlayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Skuxx
{

    public static int COLOR = -1;
    private static Skuxx instance;
    private static String user;
    private double build = 2.0;
    private Myself myself;
    public static xyz.erik.skuxx.ui.click.Console console = null;
    private FriendManager friendManager;
    private NetworkUtils networkUtils;
    private ModManager modManager = new ModManager();
    private CommandManager commandManager = new CommandManager();
    private ConfigManager configManager = new ConfigManager();
    private SkuxxPlayer skuxxPlayer = new SkuxxPlayer();
    public static List<SkuxxPlayer> skuxxPlayerList = new ArrayList<>();

    public void onStart() {
        setupDiscord();
        this.networkUtils = new NetworkUtils();
        friendManager = new FriendManager();
        friendManager.start();
        IRCCommands.start();

        commandManager.onStart();
        new Render();
        new Cmd();
        Display.setTitle("Skuxx (build: " + build + ") - Release");
        SkuxxConfig.start();
        configManager.start();
        myself = new Myself();
        myself.setVerbose(true);
        new NotificationManager();
        try {
            myself.connect("chat.freenode.net");
        }catch (IOException e){}catch (IrcException asd){}
        myself.joinChannel("#skuxx772");
      //      myself.changeNick(Skuxx.getUser());
        myself.sendMessage("skuxx a giris yapti.");
        modManager.start();
        try {
            myself.sendMessage("@! " + Myself.getHWID());
        }catch (NoSuchAlgorithmException e){}catch (UnsupportedEncodingException e){}
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    myself.sendMessage("@!givecustomsetting");
                }catch (InterruptedException e){}
            }
        }).start();

        presence.state = "User: " + getUser();
        lib.Discord_UpdatePresence(presence);


    }

    public static DiscordRPC lib = DiscordRPC.INSTANCE;
    public NetworkUtils getNetworkUtils() {
        return networkUtils;
    }
    public FriendManager getFriendManager() {
        return friendManager;
    }
    public static SkuxxPlayer getSkuxxUser(String name) {
        for (SkuxxPlayer sPlayer : skuxxPlayerList) {
            if (sPlayer.getName().replace(" ","").equalsIgnoreCase(name) || sPlayer.getIngameName().replace(" ", "").equalsIgnoreCase(name)) {
                return sPlayer;
            }
        }
        return null;
    }
    public SkuxxPlayer getSkuxxPlayer() {
        return skuxxPlayer;
    }
    public void updateSkuxxPlayer() {
        String setting = "@!getinformation " + Skuxx.getUser() + "::" + Minecraft.getMinecraft().session.getUsername() +"::" +Skuxx.getInstance().getSkuxxPlayer().isChild()+ "::" + Skuxx.getInstance().getSkuxxPlayer().isHaveEar() + "::" + Skuxx.getInstance().getSkuxxPlayer().isRiding()
                +"::"+ Skuxx.getInstance().getSkuxxPlayer().getCape();
        Skuxx.getInstance().getMyself().sendMessage(setting);
        System.out.println(setting);
        System.out.println("Informations sent succesfully...");
    }

    public double getBuild() {
        return build;
    }

    public static Skuxx getInstance() {
        return instance == null ? instance = new Skuxx() : instance;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void onShutDown() {
        friendManager.save();
        getModManager().saveMods();
        configManager.save();
    }
    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static void setupDiscord() {
        String applicationId = "495293754052313108";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Playing Skuxx";
        Skuxx.presence.state = "Loading Skuxx";
        presence.smallImageKey = "hsii81s2";
        presence.largeImageKey = "hsii81s2";
        presence.endTimestamp = 10;
        presence.partyMax = 30;
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public static void updateOnlinePartyUsers(int size) {

        Skuxx.presence.partySize = size;
    }

    public static void askforCustomSettings(String ip) {
        getInstance().myself.sendMessage("@!givecustomsetting " + ip);
    }

    public static void setUser(String user) {
        Skuxx.user = user;
    }

    public static String getUser() {
        return user;
    }

    public void log(String text) {
        System.out.println(text);
    }

    public Myself getMyself() {
        return myself;
    }
}
