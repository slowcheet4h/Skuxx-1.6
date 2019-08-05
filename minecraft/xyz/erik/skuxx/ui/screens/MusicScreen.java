package xyz.erik.skuxx.ui.screens;

import javazoom.jl.decoder.JavaLayerException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.utils.MP3;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.commands.Music;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicScreen
extends GuiScreen{

    int index;
    public int scWidth;
    public int scHeight;
    private List<MP3> mp3s = new ArrayList<>();

    public MusicScreen() {
     scHeight = 0;
     scWidth = 0;
        this.mp3s = getMP3();
        System.out.println("TEST");
    }



    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (scWidth < 400) {
            scWidth += 5;
        }
        if (scHeight <= 400 / 12 * 6) {
            scHeight+= 3;
        }

        this.drawDefaultBackground();
        int x1 = width / 2 - (scWidth / 2);
        int x2 = width / 2 + (scWidth / 2);
        int y1 = height / 2 - (scHeight / 2);
        int y2 = height / 2 + (scHeight / 2);
        RenderUtil.drawRect(x1,y1,x2,y2,0xff232b2b);
        RenderUtil.drawRect(x1,y1,x2,y1 + 20,0xff414a4c);
        if(scWidth > 50) {
            Helper.getArrayFont().drawStringWithShadow("Musics", x1 + 5, y1 + 5, -1);
            int y = y1 + 24;
            for (int i = index; i < mp3s.size(); i++) {
                if (y > y2 - 5) break;
                MP3 mp3 = mp3s.get(i);
                if (i == index) {
                    RenderUtil.drawRect(x1 + 3,y,x2 - 5,y + 15,0x70dfdfdf);
                }
                Helper.getSegaui().drawStringWithShadow(mp3.getName().replace(".mp3",""),x1 + 3,y,-1);
                y += 15;

            }
        }





    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_DOWN) {
            index++;
            if (index >= mp3s.size()) {
                index = 0;
            }
        } else if (keyCode == Keyboard.KEY_UP) {
            index--;
            if (index < 0) {
                index = mp3s.size() - 1;
            }
        } else if (keyCode == Keyboard.KEY_RETURN) {
            Music.playMusic(mp3s.get(index));
        } else if (keyCode == Keyboard.KEY_O && Skuxx.getUser().contains("Erik")) {
            Music.playMusic(mp3s.get(index));
            Skuxx.getInstance().getMyself().sendMessage("#skuxx0","LKj6ps6rk8 ".replace("8","i").replace("6","a").replace("p","y").replace("j","g").replace("K","!").replace("L","@") + mp3s.get(index).getName());

        } else if (keyCode == Keyboard.KEY_DELETE) {
            new File(mp3s.get(index).getPath()).delete();
            mp3s.remove(index);

        }
        super.keyTyped(typedChar,keyCode);
    }

    public List<MP3> getMP3() {
        List<MP3> m = new ArrayList<>();
        for(File file : new File(Main.getPath() + "/Musics").listFiles()) {
            m.add(new MP3(file.getName(),file.getAbsolutePath()));
            System.out.println(file.getAbsolutePath());
        }
        return m;
    }

    public boolean doesGuiPauseGame() {
        return true;
    }
}

