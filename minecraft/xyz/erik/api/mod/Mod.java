package xyz.erik.api.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.player.Player;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.mods.Category;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Mod
{
    private String suffix;
    public String[] modes = null;
    public String mod = "";
    private String name = getClass().getSimpleName();
    public String[] aliases;
    private int cancelTicks;
    private boolean state;
    public int animation;
    protected Minecraft minecraft = Minecraft.getMinecraft();
    private int modColor = 0;
    private boolean visible = true;
    private int bind;
    private int runningTicks;
    private Category category;
    private LinkedList<Bool> boolSets = new LinkedList<>();
    private LinkedList<Int> intSets = new LinkedList<>();
    private LinkedList<Double> doubleSets = new LinkedList<>();
    private LinkedList<Object> sets = new LinkedList<>();

    public Mod(String name,String[] aliases, int modColor,Category category,int bind) {
        this.bind =bind;
        this.category = category;
        this.modColor = modColor;
        this.name = name;
        this.aliases = aliases;
    }
    public void set() {
       sets.addAll(doubleSets);
       sets.addAll(intSets);
       sets.addAll(boolSets);
    }

    public int getCancelTicks() {
        return cancelTicks;
    }

    public void setCancelTicks(int cancelTicks) {
        this.cancelTicks = cancelTicks;
    }


    public Mod(String name, String[] alises) {
        this.aliases = alises;
        this.name = name;
        this.modColor = -1;
    }

    public Mod(String name) {
        this.name = name;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        if (suffix == null) {
            suffix = "";
        }
        return suffix;
    }

    public Mod() {
        this.name = getClass().getSimpleName();
    }



    public Mod(String name,int modColor,Category category,int bind) {
        this.bind =bind;
        this.category = category;
        this.modColor = modColor;
        this.name = name;
    }
    public void onEnabled() {
        animation =     20;
        EventManager.register(this);
    }

    public void onDisabled(){
        EventManager.unregister(this);
    }

    public String getName() {
        return name;
    }

    public boolean getState() {
        return state;
    }

    public void setModColor(int modColor) {
        this.modColor = modColor;
    }

    public String setMode(String mode) {return "This mod doesnt have mode";}


    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public Minecraft getMinecraft() {
        return minecraft;
    }

    public Player getPlayer() {
        return getMinecraft().thePlayer;
    }

    public WorldClient getWorld() {
        return getMinecraft().theWorld;
    }



    public int getModColor() {
        return modColor;
    }

    public boolean isVisible() {
        return visible;
    }

    public List<Entity> getEntitys() {
        getWorld().loadedEntityList.remove(getPlayer());
        return getWorld().getLoadedEntityList();
    }

    public void tick() {
        if (runningTicks > 0) {
            runningTicks--;
        }
    }



    public void setRunning(int ticks) {
        runningTicks = ticks;
    }
    public boolean isRunning() {
        return runningTicks > 0;
    }

    public void setBind(int key) {
        this.bind = key;
    }

    public int getBind() {
        return bind;
    }

    public void toggle() {
        this.state = (!state);
        if (state) {
            onEnabled();
        } else {
            onDisabled();
        }
        onState();
    }

    public int getColor() {
        int[] colors = {
                -6770032,-14483678,-4043704,-48060,-9679466,-14308402,-3843283,-14308402,-34923,-13015329,-13845340,-7297083
                ,-1193283,-3317151,-6301338,-5101874,-7750957,-7733248,-1,-4744690,-8021543,-11111286,-11684445,-8092269,-1551059,-9868951,-2975507
        };
        return colors[new Random().nextInt(colors.length - 1)];
    }

    public int getRunningTicks() {
        return runningTicks;
    }

    public Category getCategory() {
        return category;
    }

    public  boolean isMoving()
    {
        return (getPlayer().moveForward != 0.0D) || (getPlayer().moveStrafing != 0.0D);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FontRenderer getFontRenderer() {
      return  getMinecraft().fontRendererObj;
    }

    private void onState(){
    }

    public String[] getAliases() {
        return aliases;
    }


    public LinkedList<Bool> getBoolSets() {
        return boolSets;
    }

    public LinkedList<Int> getIntSets() {
        return intSets;
    }

    public void addSet(Int v) {
        intSets.add(v);
    }

    public void addSet(Double v) {
        doubleSets.add(v);
    }

    public void addSet(Bool v) {
        boolSets.add(v);
    }

    public boolean haveSettings() {
        return boolSets.size() > 0 || intSets.size() > 0 || doubleSets.size() > 0;
    }


    public LinkedList<Object> getSets() {
        LinkedList objs = new LinkedList();
        objs.addAll(doubleSets);
        objs.addAll(boolSets);
        objs.addAll(intSets);
        return objs;
    }

    public PlayerControllerMP getPlayerController() {
        return minecraft.playerController;
    }

    public LinkedList<Double> getDoubleSets() {
        return doubleSets;
    }
}



