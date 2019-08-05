package xyz.erik.skuxx.irc.irccmd;

public class IRCCommand {


    public String trigger;

    public IRCCommand(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
    }

    public void run(String text) {

    }
}