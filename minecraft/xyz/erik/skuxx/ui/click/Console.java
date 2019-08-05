package xyz.erik.skuxx.ui.click;

import org.lwjgl.input.Keyboard;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.helpers.Location;
import xyz.erik.api.utils.RenderUtil;
import xyz.erik.skuxx.mods.untoggleable.Cmd;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Console {

    private int x,cX;
    private int y,cY;
    private int width;
    private int height;

    private int COVER_COLOR = 0xff4C5844;
    private int BACK_COLOR = 0xFF3E4637;
    private int X_COLOR = 0x889180;
    private int TEXT_COLOR = 0xFFFFFF;
    public Console() {
        width = 300;
        height = width / 12 * 9;
    }
    private List<String> texts = new ArrayList<>();




    public void render(int width,int height) {
        RenderUtil.drawRect(x,y,x+this.width, y+30,COVER_COLOR);

        RenderUtil.drawRect(x,y + 15,x+this.width - 5,y + this.height,BACK_COLOR);
        RenderUtil.drawRect(x,y,x + 5,y+this.height,COVER_COLOR);
        RenderUtil.drawRect(x + this.width - 5,y,x + this.width,y+this.height,COVER_COLOR);
        RenderUtil.drawRect(x,y+this.height - 5,x+  this.width,y+this.height,COVER_COLOR);
        RenderUtil.drawRect(x,y+this.height - 20,x+  this.width,y+this.height - 15,COVER_COLOR);
        Helper.getArrayFont().drawStringWithShadow("Console",x + 32,y + 15 / 4,TEXT_COLOR);
        int y = this.y + 16;
        for(String text : texts) {
            Helper.getArrayFont().drawStringWithShadow(text,x + 6,y,TEXT_COLOR);
            y+= 10;
        }
        Helper.getArrayFont().drawStringWithShadow(text,x + 6,this.y + this.height - 15,TEXT_COLOR);
    }

    public String text = "";

    public void mousePressed(int mouseX,int mouseY) {
        cX = mouseX;
        cY = mouseY;
    }
    public void mouseDragged(int mouseX,int mouseY){
        if(isOver(mouseX,mouseY)) {
            x = mouseX - width / 2;
            y = mouseY - height / 2;
        }
    }

    public void keyTyped(int key) {
        if(key == Keyboard.KEY_BACK) {
            text = text.substring(0,text.length() - 1);
            return;
        }
        if(key == Keyboard.KEY_ESCAPE) {
            Helper.mc().currentScreen.onGuiClosed();
            return;
        }
        if(key == Keyboard.KEY_RETURN) {
            Command cmd = Cmd.getCommand(text);
            addText(cmd.execute(text,text.split(" ")));
            text = "";
            return;
        }
        if(key == Keyboard.KEY_SPACE) {
            text += "";
            return;
        }
        String ch = Keyboard.getKeyName(key);
        boolean isOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if(isOn)
        text += ch;
        else
            text += ch.toLowerCase();

    }

    public void addText(String text) {
        texts.add(text);
        if (texts.size() > 20) {
            texts.remove(0);
        }
    }

    public void mouseReleased(int mouseX,int mouseY){}


    public boolean isOver(int mouseX,int mouseY) {
        return x < mouseX && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveX(int x) {
        setX(this.x + x);
    }

    public void moveY(int y) {
        setX(this.y + y);
    }

    public void move(int x, int y) {
        moveX(x);
        moveY(y);
    }
}
