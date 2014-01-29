package fr.xebia.xebay.domain;

import java.util.*;

import static java.lang.String.format;

public class BidEngine {
    private static final int DEFAULT_TIME_TO_LIVE = 10000;

    private final List<BidEngineListener> listeners = new ArrayList<>();
    private final Items items;
    private final Expirable bidOfferExpiration;
    private final Queue<BidOfferToSell> bidOffersToSell;

    private int timeToLive;
    private BidOffer bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> bidOffer == null || bidOffer.isExpired();
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = new BidOffer(this.items.next(), timeToLive);
    }

    public BidEngine(Items items, Expirable bidOfferExpiration) {
        this.items = items;
        this.bidOfferExpiration = bidOfferExpiration;
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = new BidOffer(this.items.next(), timeToLive);
    }

    public BidOffer currentBidOffer() {
        nextBidOfferIfExpired();
        return bidOffer;
    }

    public BidOffer bid(User user, String name, double value, double increment) throws BidException {
        nextBidOfferIfExpired();
        if (bidOffer == null) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        bidOffer.increment(name, value, increment, user);
        listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferBidded(bidOffer, user));
        return bidOffer;
    }

    public void offer(User user, String itemName, double initialValue) {
        nextBidOfferIfExpired();

        Item item;
        try {
            item = items.get(itemName);
        } catch (NoSuchElementException e) {
            throw new BidException(format("item \"%s\" doesn't exist", itemName));
        }
        if (bidOffer != null && bidOffer.getItem().equals(item)) {
            throw new BidException(format("item \"%s\" is the current offer thus can't be offered", itemName));
        }
        if (!user.equals(item.getOwner())) {
            throw new BidException(format("item \"%s\" doesn't belong to user \"%s\"", item, user));
        }
        bidOffersToSell.offer(new BidOfferToSell(item, initialValue));
    }

    public void addListener(BidEngineListener bidEngineListener) {
        nextBidOfferIfExpired();
        listeners.add(bidEngineListener);
    }

    private void nextBidOfferIfExpired() {
        if (bidOfferExpiration.isExpired()) {
            if (bidOffer != null) {
                bidOffer.resolve();
                listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferResolved(bidOffer, bidOffer.getFutureBuyer()));
            }
            if (bidOffersToSell.isEmpty()) {
                Item nextItem = items.next();
                if (nextItem == null) {
                    bidOffer = null;
                } else {
                    bidOffer = new BidOffer(nextItem, timeToLive);
                }
            } else {
                bidOffer = bidOffersToSell.poll().toBidOffer(timeToLive);
            }
            listeners.forEach(bidEngineListener -> bidEngineListener.onNewBidOffer(bidOffer));
        }
    }
}
