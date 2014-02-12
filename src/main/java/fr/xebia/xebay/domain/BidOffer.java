package fr.xebia.xebay.domain;

public class BidOffer {
    String itemName;
    double initialValue;
    double currentValue;
    long timeToLive;
    String ownerName;
    String futureBuyerName;

    public BidOffer() {
    }

    public BidOffer(Item item, double initialValue, double currentValue, long timeToLive, User futureBuyer) {
        this.timeToLive = timeToLive;
        this.itemName = item.getName();
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.ownerName = item.getOwner() == null ? null : item.getOwner().getName();
        this.futureBuyerName = futureBuyer == null ? null : futureBuyer.getName();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFutureBuyerName() {
        return futureBuyerName;
    }

    public void setFutureBuyerName(String futureBuyerName) {
        this.futureBuyerName = futureBuyerName;
    }
}
