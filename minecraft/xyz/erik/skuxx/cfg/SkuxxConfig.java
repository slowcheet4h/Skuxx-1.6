package xyz.erik.skuxx.cfg;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.main.Main;
import net.minecraft.util.ResourceLocation;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.utils.FileUtils;
import xyz.erik.skuxx.enums.Blockhit;

import java.io.File;
import java.util.List;
import java.util.Random;

public class SkuxxConfig {
    private static String PREFIX = ">";
    private static Blockhit blockhit = Blockhit.Skuxx;
    private static Location location = Location.CHEST;
    public static boolean reverseHead = false;
    private static boolean randomBackGround = true;
    public static void start() {
        SkuxxConfig.setBlockhit(Blockhit.Skuxx);
        String bck = "/skuxx/background/skuxxbackground" + new Random().nextInt(7) + ".jpg";
        System.out.println(bck);
        GuiMainMenu.BG1 = new ResourceLocation(bck);
    }

    public static void setBlockhit(Blockhit blockhit) {
        SkuxxConfig.blockhit = blockhit;
    }

    public static boolean isRandomBackGround() {
        return randomBackGround;
    }

    public static Blockhit getBlockhit() {
        return blockhit;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static Location getLocation() {
        return location;
    }



}

