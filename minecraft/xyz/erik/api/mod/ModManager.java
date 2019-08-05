package xyz.erik.api.mod;

import xyz.erik.api.utils.FileUtils;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.EventTick;
import xyz.erik.skuxx.event.events.KeyPressEvent;
import xyz.erik.skuxx.mods.NONE.CJSounds;
import xyz.erik.skuxx.mods.NONE.NoBob;
import xyz.erik.skuxx.mods.combat.*;
import xyz.erik.skuxx.mods.exploits.*;
import xyz.erik.skuxx.mods.player.*;
import xyz.erik.skuxx.mods.move.*;
import xyz.erik.skuxx.mods.untoggleable.Render;
import xyz.erik.skuxx.mods.visual.Brightness;
import xyz.erik.skuxx.mods.visual.Gui;
import xyz.erik.skuxx.mods.visual.NameTags;
import xyz.erik.skuxx.mods.visual.VRCam;

import java.io.File;
import java.util.*;

public class ModManager
{
    private File file = FileUtils.getConfigFile("mods");
    private List<Mod> mods = new ArrayList<>();
    public void start(){
        addMod(new Scaffold());
        addMod(new Brightness());
        addMod(new Breaker());
addMod(new Terrain());
addMod(new NCPFly());
        addMod(new VehicleControl());
        addMod(new IRC());
        addMod(new AutoTool());
        addMod(new MagnetPunch());
        //addMod(new Nuker());
   //     addMod(new SpeedBuilders());
       addMod(new BowAim());
        addMod(new Meme());
        addMod(new Gui());
        addMod(new Jesus());
        addMod(new Aura());
        addMod(new AutoHeal());
        addMod(new AutoArmor());
        addMod(new VRCam()  );
        addMod(new Sprint());
        addMod(new Speed());
        addMod(new Sneakers());
        addMod(new Inventory());
        addMod(new pSilent());
        addMod(new NoClip());
        addMod(new Fly());
        addMod(new Damage());
        addMod(new NoSlow());
        addMod(new Regen());
        addMod(new Criticals());
        addMod(new InfinityAura());
        addMod(new Blink());
        addMod(new NameTags());
        addMod(new Speedmine());
        addMod(new NoFall());
        addMod(new Setback());
        addMod(new ChestStealer());
        addMod(new LongJump());
        addMod(new Freecam());
        addMod(new Firion());
        addMod(new ArmorSwap());
        addMod(new FastUse());
        addMod(new VerticalPhase());
        addMod(new AntiVelocity());
        addMod(new BowFly());
        addMod(new ShootBow());
        addMod(new AntiWeb());
        addMod(new Render());
        addMod(new FakeAura());
        addMod(new Pantregen());
        addMod(new Beyblade());
        addMod(new NoBob());
        addMod(new CJSounds());
        addMod(new Step());
        addMod(new AutoGapple());
     //TOOD: IDK   addMod(new Gui());

        loadMods();
        saveMods();
            loadColors();
        EventManager.register(this);
    }

    @EventTarget
    private void onTick(EventTick eventTick) {
        for (Mod mod : Skuxx.getInstance().getModManager().getMods()) {
            mod.tick();
            if (mod.getCancelTicks() > 0) mod.setCancelTicks(mod.getCancelTicks() - 1);
        }
    }

    private void addMod(Mod mod) {
        Skuxx.getInstance().log("Mod loaded " + mod.getName());
        mods.add(mod);
    }


    public Mod getMod(String name) {
        for (Mod mod:
             mods) {
            if (mod.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return mod;
            }
            if(mod.getAliases() != null)
            for(String alias : mod.getAliases()) {
                if (alias.toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                    return mod;
                }
            }
        }
        return new Mod(null,0,null,0);
    }


    public void loadMods() {
        List<String> strings = FileUtils.read(file);
        for (String string :strings) {
            String[] split = string.split(":");
            Mod mod = getMod(split[0]);
            //Aura:enabled:18:visible
            System.out.println(string);
                boolean visible = split[3].equalsIgnoreCase("visible");
            boolean enabled = (split[1].equalsIgnoreCase("enabled"));
            int keyCode = (Integer.parseInt(split[2]));
            mod.setBind(keyCode);
            mod.setVisible(visible);
            if (mod.getState() != enabled) {
                mod.toggle();
            }
            mod.set();
        }
    }

    public void saveMods() {
        List<String> strings = new ArrayList<>();
        for (Mod mod:
             getMods()) {
            String modName = mod.getName();
            String enabled = mod.getState() ? "enabled" : "disabled";
            String modVisible = mod.isVisible() ? "visible" : "invisible";
            int modBind = mod.getBind();
            strings.add(modName + ":" + enabled + ":" + modBind + ":" + modVisible);
        }
        FileUtils.write(file,strings,true);
    }


    public void loadColors() {
        for (Mod mod : mods) {
            mod.setModColor(mod.getColor());
        }
    }





    @EventTarget
    private void onKey(KeyPressEvent event) {
        for (Mod mod:
             getMods()) {
            if (mod.getBind() == event.getKey()) {
                mod.toggle();
                break;
            }

        }
    }
    /*     Collections.sort(mods, new Comparator<Mod>() {
             public int compare(Mod mod1, Mod mod2) {
                 return Helper.getFont().getStringWidth(net.minecraft.util.StringUtils.stripControlCodes(mod1.getName())) >
                         Helper.getFont().getStringWidth(net.minecraft.util.StringUtils.stripControlCodes(mod2.getName()))
                         ? -1 : Helper.getFont().getStringWidth(net.minecraft.util.StringUtils.stripControlCodes(mod1.getName()))
                         < Helper.getFont().getStringWidth(net.minecraft.util.StringUtils.stripControlCodes(mod2.getName())) ? 1 : 0;
             }
         });*/
    public List<Mod> getMods() {

        return mods;
    }
}
