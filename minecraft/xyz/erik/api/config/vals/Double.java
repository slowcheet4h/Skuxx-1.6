package xyz.erik.api.config.vals;

public class Double
{
    private String name;
    private double value;
    private double minValue;
    private double maxValue;

    public Double(String name, double value, double minValue,double maxValue) {
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if(value > getMaxValue())
            this.value = getMaxValue();
        else if(value < getMinValue())
            this.value = getMinValue();
        else
            this.value = value;
    }
}
