package fr.xebia.xebay.domain;

public class BidOfferToSell {
    private final Item item;
    private final double initialValue;

    public BidOfferToSell(Item item, double initialValue) {
        this.item = item;
        this.initialValue = initialValue;
    }

    public BidOffer toBidOffer(int initialTimeToLive) {
        return new BidOffer(item, initialValue, initialTimeToLive);
    }
}
