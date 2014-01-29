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
    private Optional<BidOffer> bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> !bidOffer.isPresent() || bidOffer.get().isExpired();
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new BidOffer(this.items.next(), timeToLive));
    }

    public BidEngine(Items items, Expirable bidOfferExpiration) {
        this.items = items;
        this.bidOfferExpiration = bidOfferExpiration;
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new BidOffer(this.items.next(), timeToLive));
    }

    public BidOffer currentBidOffer() {
        nextBidOfferIfExpired();
        return bidOffer.isPresent() ? bidOffer.get() : null;
    }

    public BidOffer bid(User user, String name, double value, double increment) throws BidException {
        nextBidOfferIfExpired();
        bidOffer.orElseThrow(() -> new BidException(format("current item to bid is not \"%s\"", name)))
                .increment(name, value, increment, user);
        listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferBidded(bidOffer.get(), user));
        return bidOffer.get();
    }

    public void offer(User user, String itemName, double initialValue) {
        nextBidOfferIfExpired();

        Item item;
        try {
            item = items.get(itemName);
        } catch (NoSuchElementException e) {
            throw new BidException(format("item \"%s\" doesn't exist", itemName));
        }
        if (bidOffer.isPresent() && bidOffer.get().getItem().equals(item)) {
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
            bidOffer.ifPresent((bidOffer) -> {
                bidOffer.resolve();
                listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferResolved(bidOffer, bidOffer.getFutureBuyer()));
            });
            bidOffer = nextBidOffer();
            bidOffer.ifPresent((bidOffer) -> listeners.forEach(bidEngineListener -> bidEngineListener.onNewBidOffer(bidOffer)));
        }
    }

    private Optional<BidOffer> nextBidOffer() {
        if (!bidOffersToSell.isEmpty()) {
            return Optional.of(bidOffersToSell.poll().toBidOffer(timeToLive));
        }
        Item nextItem = items.next();
        if (nextItem == null) {
            return Optional.empty();
        } else {
            return Optional.of(new BidOffer(nextItem, timeToLive));
        }
    }
}
