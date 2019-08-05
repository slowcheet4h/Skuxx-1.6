package xyz.erik.api.friend;

public class Friend {

    private String alias;
    private String name;

    public Friend(String name,String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
