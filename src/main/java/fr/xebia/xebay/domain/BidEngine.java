package fr.xebia.xebay.domain;

import java.util.*;

import static java.lang.String.format;

public class BidEngine {
    private static final int DEFAULT_TIME_TO_LIVE = 10000;

    final List<BidEngineListener> listenerList = new ArrayList<>();

    private final Items items;
    private final Expirable bidOfferExpiration;
    private final Queue<BidOfferToSell> bidOffersToSell;

    private int timeToLive;
    private BidOffer bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> bidOffer.isExpired();
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
        bidOffer.increment(name, value, increment, user);
        listenerList.forEach(bidEngineListener -> bidEngineListener.onBidOffer(bidOffer));
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
        if (bidOffer.getItem().equals(item)) {
            throw new BidException(format("item \"%s\" is the current offer thus can't be offered", itemName));
        }
        if (!user.equals(item.getOwner())) {
            throw new BidException(format("item \"%s\" doesn't belong to user \"%s\"", item, user));
        }
        bidOffersToSell.offer(new BidOfferToSell(item, initialValue));
    }

    private void nextBidOfferIfExpired() {
        if (bidOfferExpiration.isExpired()) {
            bidOffer.resolve();
            if (bidOffersToSell.isEmpty()) {
                // FIXME gérer le cas où il n'y a plus d'items
                bidOffer = new BidOffer(items.next(), timeToLive);
            } else {
                bidOffer = bidOffersToSell.poll().toBidOffer(timeToLive);
            }
            listenerList.forEach(bidEngineListener -> bidEngineListener.onBidOffer(bidOffer));
        }
    }

    public void addListener(BidEngineListener bidEngineListener) {
        listenerList.add(bidEngineListener);
    }

}
