package fr.xebia.xebay.domain;

public class BidEngine {
    private static final int DEFAULT_TIME_TO_LIVE = 10000;

    private final Items items;
    private final Expirable bidOfferExpiration;

    private int timeToLive;
    private BidOffer bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> bidOffer.isExpired();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = new BidOffer(this.items.next(), timeToLive);
    }

    public BidEngine(Items items, Expirable bidOfferExpiration) {
        this.items = items;
        this.bidOfferExpiration = bidOfferExpiration;
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = new BidOffer(this.items.next(), timeToLive);
    }

    public BidOffer currentBidOffer() {
        nextBidOfferIfExpired();
        return bidOffer;
    }

    public BidOffer bid(User user, String name, double value, double increment) throws BidException {
        nextBidOfferIfExpired();
        return bidOffer.increment(name, value, increment, user);
    }

    private void nextBidOfferIfExpired() {
        if (bidOfferExpiration.isExpired()) {
            bidOffer.resolve();
            bidOffer = new BidOffer(items.next(), timeToLive);
        }
    }
}
