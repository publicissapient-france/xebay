package fr.xebia.xebay.domain;

public class BidOffer {
    //item
    String itemName;
    String itemCategory;
    String ownerName;
    double initialValue;

    double currentValue;
    long timeToLive;
    //bidder
    String futureBuyerName;

    public BidOffer() {
    }

    public BidOffer(String itemName, String itemCategory, double initialValue, double currentValue, long timeToLive, String ownerName, String futureBuyerName) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.timeToLive = timeToLive;
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.ownerName = ownerName;
        this.futureBuyerName = futureBuyerName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getFutureBuyerName() {
        return futureBuyerName;
    }
}
