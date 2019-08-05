package xyz.erik.api.config.vals;

public class Bool
{
    private boolean state;
    private String name;

    public Bool(String name, boolean state) {
        this.state = state;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
