package xyz.erik.skuxx.mods.untoggleable;

import org.lwjgl.input.Keyboard;
import xyz.erik.api.command.Command;
import xyz.erik.api.helpers.Helper;
import xyz.erik.api.mod.Mod;
import xyz.erik.api.notification.Notification;
import xyz.erik.api.notification.NotificationManager;
import xyz.erik.skuxx.Skuxx;
import xyz.erik.skuxx.commands.Setting;
import xyz.erik.skuxx.event.EventManager;
import xyz.erik.skuxx.event.EventTarget;
import xyz.erik.skuxx.event.events.KeyPressEvent;
import xyz.erik.skuxx.event.events.SendingMessageEvent;
import xyz.erik.skuxx.ui.screens.MusicScreen;

public class Cmd
{
    public Cmd() {
        EventManager.register(this);
    }

    @EventTarget
    private void send(SendingMessageEvent event) {
        if (event.getMessage().startsWith(">")) {
            event.setCancelled(true);
            Command command = null;
            String message = event.getMessage().split(">")[1];

            command = getCommand(message);
           String output = command.execute(message,message.split(" "));
            if (output.length() > 2)
            NotificationManager.addNotification(new Notification(output,false,25));


        }
    }

    public static Command getCommand(String message) {
        for (Command cmd : Skuxx.getInstance().getCommandManager().getCommandList())
        {
            for (String com : cmd.getTriggers()) {
                if (message.toLowerCase().startsWith(com.toLowerCase())) {
                   return cmd;
                }
            }
        }
        return new Setting();
    }



    @EventTarget
    private void onKeyPress(KeyPressEvent e) {
        if (e.getKey() == Keyboard.KEY_M && Helper.mc().gameSettings.keyBindSneak.pressed) {
            Helper.mc().displayGuiScreen(new MusicScreen());
        }
    }
}
