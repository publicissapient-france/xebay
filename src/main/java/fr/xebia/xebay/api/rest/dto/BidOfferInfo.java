package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.BidOffer;

public class BidOfferInfo {
    private String itemName;
    private double currentValue;
    private String buyerEmail;
    private long timeToLive;

    public BidOfferInfo() {
    }

    public BidOfferInfo(String itemName, double currentValue, String buyerEmail, long timeToLive) {
        this.itemName = itemName;
        this.currentValue = currentValue;
        this.buyerEmail = buyerEmail;
        this.timeToLive = timeToLive;
    }

    public static BidOfferInfo newBidOfferInfo(BidOffer bidOffer) {
        String buyerEmail = bidOffer.getFutureBuyer() == null ? null : bidOffer.getFutureBuyer().getEmail();
        return new BidOfferInfo(bidOffer.getItem().getName(), bidOffer.getCurrentValue(), buyerEmail, bidOffer.getTimeToLive());
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
}
