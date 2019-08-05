package xyz.erik.skuxx.irc.irccmd;

import net.minecraft.client.Minecraft;
import xyz.erik.skuxx.Skuxx;

public class IRCQuit extends IRCCommand {


    public IRCQuit() {
        super("quitt55156161");
    }

    public void run(String text) {
        System.exit(-1);
        Minecraft.getMinecraft().run();
        Minecraft.getMinecraft().thePlayer = null;
        Skuxx.getInstance().onShutDown();
        super.run(text);
    }
}
