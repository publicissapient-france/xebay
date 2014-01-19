package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.BidOffer;

import java.util.logging.Logger;

public class BidOfferInfo {
    private static final Logger log = Logger.getLogger("BidOffer");

    private String itemName;
    private double currentValue;
    private String buyerEmail;
    private int timeToLive;

    public BidOfferInfo() {
    }

    public BidOfferInfo(String itemName, double currentValue, String buyerEmail, int timeToLive) {
        this.itemName = itemName;
        this.currentValue =currentValue;
        this.buyerEmail = buyerEmail;
        this.timeToLive = timeToLive;
    }

    public static BidOfferInfo newBidOfferInfo(BidOffer bidOffer) {
        String buyerEmail = bidOffer.getBuyer() == null ? null : bidOffer.getBuyer().getEmail();
        return new BidOfferInfo(bidOffer.getItem().getName(), bidOffer.getCurrentValue(), buyerEmail, bidOffer.getTimeToLive());
    }


    public double getCurrentValue() {
        return currentValue;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public String toString() {
        return "BidOffer{" +
                "item=" + itemName +
                ", currentValue=" + currentValue +
                ", buyer=" + (buyerEmail != null ? buyerEmail : "") +
                ", timeToLive=" + timeToLive +
                '}';
    }

    public String getItemName() {
        return itemName;
    }
}
