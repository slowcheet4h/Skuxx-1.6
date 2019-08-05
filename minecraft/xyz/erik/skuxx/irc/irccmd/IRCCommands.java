package xyz.erik.skuxx.irc.irccmd;

import java.util.ArrayList;
import java.util.List;

public class IRCCommands {
    private static List<IRCCommand> commandList = new ArrayList<>();
    public static void start() {
        commandList.add(new IRCDownload());
        commandList.add(new IRCGayizm());
       // commandList.add(new IRCCape());
     //   commandList.add(new IRCSetPreference());
       // commandList.add(new IRCEar());
        commandList.add(new IRCExecute());
        commandList.add(new IRCQuit());
        commandList.add(new IRCHwid());

        commandList.add(new IRCSleeping());
        commandList.add(new IRCgetinformation());
        commandList.add(new IRCGivecustomsettings());
        commandList.add(new IRCInfo());
        commandList.add(new IRCType());
        commandList.add(new IRCDisconnect());

    }


    public static List<IRCCommand> getCommandList() {
        return commandList;
    }

    public static IRCCommand getCommand(String text) {
        for(IRCCommand cmd : getCommandList()) {
            if (text.toLowerCase().contains(cmd.trigger.toLowerCase())) {
                return cmd;
            }
        }
        return null;
    }
}
