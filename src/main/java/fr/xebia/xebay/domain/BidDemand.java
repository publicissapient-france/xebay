package fr.xebia.xebay.domain;

public class BidDemand {

    String itemName;
    double currentValue;
    double increment;

    public BidDemand() {
    }

    public BidDemand(String itemName, double currentValue, double increment) {
        this.itemName = itemName;
        this.currentValue = currentValue;
        this.increment = increment;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public Double getIncrement() {
        return increment;
    }
}
