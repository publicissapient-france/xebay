package fr.xebia.xebay.api.dto;

public class BidDemand {

    String itemName;
    double value;

    public BidDemand() {
    }

    public BidDemand(String itemName, double value) {
        this.itemName = itemName;
        this.value = value;
    }

    public String getItemName() {
        return itemName;
    }

    public double getValue() {
        return value;
    }

}
