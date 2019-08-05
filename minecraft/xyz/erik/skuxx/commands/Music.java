package xyz.erik.skuxx.commands;

import javazoom.jl.decoder.JavaLayerException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.api.utils.MP3;
import xyz.erik.skuxx.ui.screens.MusicScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Music
extends Command{

    public Music() {
        super(new String[]{"musicsreen","music"},"Play Music");
    }

    private List<MP3> mp3s = new ArrayList<>();
    public String execute(String message, String[] split) {
        mp3s = getMP3();
        if(split.length == 1) {
           Minecraft.getMinecraft().displayGuiScreen(new MusicScreen());
            return "";
        }
        else {
            String requestedMusicName = message.split("music ")[1];
            System.out.println(requestedMusicName);
            for(MP3 mp3 : mp3s) {
                System.out.println(mp3.getName() + " " + requestedMusicName);
                if (mp3.getName().toLowerCase().equalsIgnoreCase(requestedMusicName.toLowerCase())) {
                    playMusic(mp3);
                    NotificationManager.addNotification(new Notification(mp3.getName() + " is now playing.",false,30));
                    break;
                }
            }



        }


        return "";
    }
    public static Thread lastThread = null;
    private static javazoom.jl.player.Player player = null;
    public static void playMusic(MP3 mp3) {
        if (player != null) player.close();
        System.out.println("Trying to start " + mp3.getName());
            lastThread =    new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        FileInputStream fileInputStream = new FileInputStream(mp3.getPath());
                        player = new javazoom.jl.player.Player(fileInputStream);
                        player.play();

                    }
                    catch (IOException | JavaLayerException localInterruptedException) {}
                }
            });

            lastThread.start();



    }
    public List<MP3> getMP3() {
        List<MP3> m = new ArrayList<>();
        for(File file : new File(Main.getPath() + "/Musics").listFiles()) {
            m.add(new MP3(file.getName(),file.getAbsolutePath()));
            System.out.println(file.getAbsolutePath());
        }
        return m;
    }


}
