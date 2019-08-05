package xyz.erik.api.config.vals;

public class Int
{
    private int value;
    private String name;
    private int maxValue;
    private int minValue;
    public Int(String name, int value,int minValue,int maxValue) {
        this.name = name;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if(value > getMaxValue())
            this.value = getMaxValue();
        else if(value < getMinValue())
            this.value = getMinValue();
        else
        this.value = value;
    }
}
