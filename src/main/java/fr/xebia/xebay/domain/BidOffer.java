package fr.xebia.xebay.domain;

import java.util.Optional;

public class BidOffer {
    String itemName;
    double initialValue;
    double currentValue;
    long timeToLive;
    String ownerEmail;
    String futureBuyerEmail;

    public BidOffer() {
    }

    public BidOffer(Item item, double initialValue, double currentValue, long timeToLive, User futureBuyer) {
        this.timeToLive = timeToLive;
        this.itemName = item.getName();
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.ownerEmail = item.getOwner() == null ? null : item.getOwner().getEmail();
        this.futureBuyerEmail = futureBuyer == null ? null : futureBuyer.getEmail();
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getFutureBuyerEmail() {
        return futureBuyerEmail;
    }

    public void setFutureBuyerEmail(String futureBuyerEmail) {
        this.futureBuyerEmail = futureBuyerEmail;
    }
}
