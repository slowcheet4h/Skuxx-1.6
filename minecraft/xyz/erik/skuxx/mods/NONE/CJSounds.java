package xyz.erik.skuxx.mods.NONE;

import net.minecraft.client.main.Main;
import org.apache.commons.io.FileUtils;
import xyz.erik.api.helpers.ErikTimer;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.api.utils.MP3;
import xyz.erik.skuxx.commands.Music;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventMotion;
import xyz.erik.skuxx.mods.move.speedmodes.Erik;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;

public class CJSounds
extends Mod
{

    private boolean installed;
    public void onEnabled() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (!new File(Main.getPath() + "/CJ").exists()) {
                        new File(Main.getPath() +"/CJ").mkdir();
                    }
                    downloadSound("http://download1481.mediafire.com/2rod39zjc5wg/z4dc1nwj51uhhd9/ohno.mp3","ohno.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1493.mediafire.com/1i43kgmb27xg/7zmd717yd9zlkuk/ohhellno.mp3","ohhellno.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1327.mediafire.com/6tsrde2p0nng/kqhlln82lquqmc2/ohshit.mp3","ohshit.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download949.mediafire.com/kvjp67brrgtg/tfod2b4vzbdboq9/aaaaah.mp3","aaaaah.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download945.mediafire.com/4b7f0cors3qg/okoboagjwmegplo/burning2.mp3","burning2.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download857.mediafire.com/z5aajj1m7dig/dcw0c21uia60ot5/idontneedthisshit.mp3","idontneedthisshit.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1654.mediafire.com/2eat9wij2i0g/ni3vp9lylvyin95/ihategravity.mp3","ihategravity.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1638.mediafire.com/76676q21c7yg/2qb0ji8ikvm2cxp/ohmotherfu.mp3","ohmotherfu.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1979.mediafire.com/k51j8v86rbqg/fd5qjd0o46vb9yq/wtf..mp3","wtf.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1585.mediafire.com/6vz0nc0zvzhg/u6hcyymxf87vwx4/burning1.mp3","burning1.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1335.mediafire.com/ccbi6x6t5lyg/2nopt5d3n467n4n/shitdam.mp3","shitdam.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1510.mediafire.com/8q81berhg4wg/nj9bdqn9wudeiw2/ohfuckshit.mp3","ohfuckshit.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1503.mediafire.com/2fd2pmwh8sng/d86861otjhtvdc9/ohshitnoo.mp3","ohshitnoo.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1488.mediafire.com/g3e2ay2ez8zg/ico26aqej2jp71x/herewegoagain.mp3","herewegoagain.mp3");
                    Thread.sleep(200);

                    downloadSound("http://download1525.mediafire.com/efdbovpuchag/3tun6bpbuz36s4p/ohmydog.mp3","ohmydog.mp3");
                    Thread.sleep(200);
                    installed = true;
                    fallSounds = new String[]{"ohno.mp3","ohhellno.mp3","aaaaah.mp3","idontneedthisshit.mp3",
                    "ihategravity.mp3","ohno.mp3","wtf.mp3","shitdam.mp3","ohfuckshit"};
                    NotificationManager.addNotification(new Notification("Download finished",false,25));
                }catch (InterruptedException e){}
            }
        }).start();

    super.onEnabled();
    }








    private boolean connected;
    private String[] fallSounds;

    private ErikTimer fallTimer = new ErikTimer();
    private ErikTimer burningTimer = new ErikTimer();
    @EventTarget
    private void onUpdate(final EventMotion e) {
        if (!installed) return;
        if (getPlayer().isBurning() && burningTimer.delay(1700)) {
            int sayi = new Random().nextInt(4);
            if (sayi == 0) {
                sayi = 1;
            }
            if (sayi > 2) sayi = 2;
            playSound("burning" + sayi + ".mp3");
            burningTimer.reset();
        }

        if (!getPlayer().onGround && getPlayer().fallDistance > 4 && fallTimer.delay(5000)) {

            String random = fallSounds[new Random().nextInt(fallSounds.length - 1)];
            playSound(random);

            fallTimer.reset();
        }
        if (!connected && getPlayer().ticksExisted > 10) {
            playSound("herewegoagain.mp3");
            connected = true;
        }
        if(getPlayer().ticksExisted < 10)connected = false;
    }




    public void playSound(String name) {
        String path = Main.getPath() + "/CJ/" + name;
        Music.playMusic(new MP3(name,path));
    }

    public void notification(String file) {
        NotificationManager.addNotification(new Notification("Downloading " + file,false,25));
    }

    private static Path download(String sourceURL, String targetDirectory) throws IOException
    {
        URL url = new URL(sourceURL);
        String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
        Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }

    private void downloadSound(String link,String name) {
        notification(name);

        if(!new File(Main.getPath() + "/CJ/" + name).exists()) {
            try {
                FileUtils.copyURLToFile(new URL(link), new File(Main.getPath() + "/CJ/" + name));
            } catch (IOException e) {
                notification("File cant be downloaded");
            }
        }
    }
}
