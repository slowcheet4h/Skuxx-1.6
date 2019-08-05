package xyz.erik.api.config;

import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.utils.FileUtils;
import xyz.erik.skuxx.Skuxx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {


    public File file;

    public void start() {
        file = FileUtils.getConfigFile("cfg");
        if (file.exists()) {
            load();
        }
        save();
    }

    public void load() {
        List<String> data = FileUtils.read(file);
        for (String line:
             data) {
            if (line.startsWith("--")) continue;
            String[] s = line.split("::");
            Mod mod = Skuxx.getInstance().getModManager().getMod(s[2]);
            if (mod == null) continue;
            switch (s[1].toLowerCase()) {
                case "int":
                    Int i = getIntSetFromMod(s[3],mod);
                    if (i != null)
                    i.setValue(Integer.parseInt(s[0]));
                    break;

                case "double":
                    Double doub =getDoubleSetFromMod(s[3],mod);
                    doub.setValue(java.lang.Double.parseDouble(s[0]));
                    break;

                case "bool":
                    Bool bool = getBoolSetFromMod(s[3],mod);
                    if (bool != null)
                    bool.setState(Boolean.parseBoolean(s[0]));
                    break;
                case "mode::":
                        mod.setMode(s[1]);
                        System.out.println(mod.getName() + "'s mod set to " + mod.mod);

                    break;
            }
        }
    }

    public void save() {
        List<String> stringList = new ArrayList<>();
        for (Mod
                mod : Skuxx.getInstance().getModManager().getMods()) {
            if (mod.mod != "") {
                stringList.add("mode::"  +mod.mod + "::" + mod.getName());
            }
            if (!mod.haveSettings()) {
                continue;
            }
            for (Bool bool : mod.getBoolSets()) {
                stringList.add(bool.isState() + "::" + "bool" + "::" + mod.getName() +"::" + bool.getName());
            }
            for (Int intSet : mod.getIntSets()) {
                stringList.add(intSet.getValue() + "::" + "int" + "::" + mod.getName() + "::" + intSet.getName());
            }
            for (Double doubleSet : mod.getDoubleSets()) {
                stringList.add(doubleSet.getValue() + "::" + "double" + "::" + mod.getName() + "::"+doubleSet.getName());
            }


        }
        FileUtils.write(file,stringList,true);
    }


    public Int getIntSetFromMod(String name, Mod mod) {
        for (Int intSet : mod.getIntSets()) {
            if (intSet.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return intSet;
            }
        }
        return null;
    }

    public Double getDoubleSetFromMod(String name, Mod mod) {
        for (Double d : mod.getDoubleSets()) {
            if (d.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return d;
            }
        }
        return null;
    }

    public Bool getBoolSetFromMod(String name, Mod mod) {
        for (Bool d : mod.getBoolSets()) {
            if (d.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                return d;
            }
        }
        return null;
    }

}
