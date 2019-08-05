package xyz.erik.skuxx.commands;

import xyz.erik.api.command.Command;
import xyz.erik.api.config.vals.Bool;
import xyz.erik.api.config.vals.Double;
import xyz.erik.api.config.vals.Int;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;

public class Setting
extends Command{

    public Setting() {
        super(new String[]{"setting","set"},"Setting");
    }


    public String execute(String message, String[] split) {
        Mod mod = Skuxx.getInstance().getModManager().getMod(split[0]);
        if (mod == null) {
            return "Mod not found";
        }
        for (Object o:
        mod.getSets()) {

            if (o instanceof Bool) {
                Bool s = (Bool) o;
                if (s.getName().toLowerCase().equalsIgnoreCase(split[1].toLowerCase())) {
                    s.setState(Boolean.parseBoolean(split[2]));

                    return mod.getName() + "'s setting " + s.getName() + " changed to " + s.isState();
                }
            }
            if (o instanceof Double) {
                Double s = (Double) o;
                if (s.getName().toLowerCase().equalsIgnoreCase(split[1].toLowerCase())) {
                    s.setValue(java.lang.Double.parseDouble(split[2]));

                    return mod.getName() + "'s setting " + s.getName() + " changed to " + s.getValue();
                }
            }
            if (o instanceof Int) {
                Int s = (Int) o;
                if (s.getName().toLowerCase().equalsIgnoreCase(split[1].toLowerCase())) {
                    s.setValue(java.lang.Integer.parseInt(split[2]));
                    return mod.getName() + "'s setting " + s.getName() + " changed to " + s.getValue();
                }
            }
        }

        if (split.length < 2) {
            if (mod.modes != null)
            NotificationManager.addNotification(new Notification( "mode <mode>",false,25));

            for(Bool boolSet : mod.getBoolSets()) {
                NotificationManager.addNotification(new Notification(boolSet.getName().toLowerCase() + " <true/false>",false,25));
            }
            for(Int intSets : mod.getIntSets()) {
                NotificationManager.addNotification(new Notification(intSets.getName().toLowerCase() + " <value>",false,25));
            }

            for(Double doubleSets : mod.getDoubleSets()) {
                NotificationManager.addNotification(new Notification(doubleSets.getName().toLowerCase() + " <value>",false,25));
            }

            return "";
        }

        if (split[1].toLowerCase().equalsIgnoreCase("mode") && split.length == 2) {
            if (mod.modes != null) {
                String modes = "";
                for (int index = 0; index < mod.modes.length; index++) {
                    modes += mod.modes[index];
                    if (index != mod.modes.length - 1) {
                        modes += ",";
                    }
                }
              return " mode <" + modes.toLowerCase() + ">";

            } else
            {
                return"this mod doesn't have modes ";
            }
        }
        if (split[1].toLowerCase().equalsIgnoreCase("mode") && split.length > 2) {

        }

        if (split[0].equalsIgnoreCase(">set") || split[0].equalsIgnoreCase("setting")) {
            split = new String[]{split[1],split[2],split[3]};
        }
        if(split[1].toLowerCase().equalsIgnoreCase("mode")) {
           return mod.setMode(split[2].toLowerCase());
        }
        if (mod == null || mod.getName().equalsIgnoreCase("")) {
            return "Mod not found " + split[0];
        }




        return "Setting not found type mod sets for all settings";
    }
}
