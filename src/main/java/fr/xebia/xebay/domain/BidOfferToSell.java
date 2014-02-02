package fr.xebia.xebay.domain;

public class BidOfferToSell {
    private final Item item;
    private final double initialValue;

    public BidOfferToSell(Item item, double initialValue) {
        this.item = item;
        this.initialValue = initialValue;
    }

    public MutableBidOffer toBidOffer(int initialTimeToLive) {
        return new MutableBidOffer(item, initialValue, initialTimeToLive);
    }
}
