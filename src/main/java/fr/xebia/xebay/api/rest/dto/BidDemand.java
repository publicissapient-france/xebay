package fr.xebia.xebay.api.rest.dto;

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

    public String getItemName() {
        return itemName;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public Double getIncrement() {
        return increment;
    }
}
