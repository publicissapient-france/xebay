package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.BidOffer;

public class BidOfferInfo {
    private String itemName;
    private double currentValue;
    private String futureBuyerEmail;
    private long timeToLive;

    public BidOfferInfo() {
    }

    public BidOfferInfo(String itemName, double currentValue, String futureBuyerEmail, long timeToLive) {
        this.itemName = itemName;
        this.currentValue = currentValue;
        this.futureBuyerEmail = futureBuyerEmail;
        this.timeToLive = timeToLive;
    }

    public static BidOfferInfo newBidOfferInfo(BidOffer bidOffer) {
        return new BidOfferInfo(bidOffer.itemName, bidOffer.currentValue, bidOffer.futureBuyerEmail.orElse(null), bidOffer.timeToLive);
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public String getItemName() {
        return itemName;
    }

    public String getFutureBuyerEmail() {
        return futureBuyerEmail;
    }
}
