package xyz.erik.api.command;

import xyz.erik.skuxx.commands.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {


    private List<Command> commandList = new ArrayList<>();

    public void onStart() {
        addCommand(new ThemeColor());
        addCommand(new StackSize());
        addCommand(new Invsee());
        addCommand(new HClip());
        addCommand(new SetCape());
        addCommand(new Setting());
       addCommand(new Toggle());
       addCommand(new AddFriend());
       addCommand(new SetPreference());
       addCommand(new Spdset());
       addCommand(new ItemScale());
       addCommand(new Guicolor());
       addCommand(new Blockhit());
        addCommand(new Bind());
        addCommand(new allwhite());
        addCommand(new SetEar());
        addCommand(new Sleep());
        addCommand(new Music());
        addCommand(new Vclip());
    }



    public Command getCommandFromClass(Class cl) {
        for (Command cmd : getCommandList()) {
            if (cmd.getClass().equals(cl)) {
                return cmd;
            }
        }
        return null;
    }


    public Command getCommandFromMessage(String message) {

        for (Command cmd : getCommandList()) {
            for (String alias : cmd.getTriggers()) {
                if (message.toLowerCase().equalsIgnoreCase(alias.toLowerCase())) {
                    return cmd;
                }
            }
         }
        return null;

    }



    private void addCommand(Command command) {
        commandList.add(command);
    }
    public List<Command> getCommandList() {
        return commandList;
    }
}
