package xyz.erik.api.command;

public abstract class Command {

    private String[] triggers;
    private String info;
    public Command(String[] triggers, String info) {
        this.info = info;
        this.triggers = triggers;
    }
    public abstract String execute(String message, String[] split);

    public String[] getTriggers() {
        return triggers;
    }

    public String getInfo() {
        return info;
    }
}
