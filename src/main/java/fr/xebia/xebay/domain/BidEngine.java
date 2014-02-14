package fr.xebia.xebay.domain;

import java.util.*;

import static java.lang.String.format;

public class BidEngine {
    public static final int DEFAULT_TIME_TO_LIVE = 10000;

    private final List<BidEngineListener> listeners = new ArrayList<>();
    private final Items items;
    private final Expirable bidOfferExpiration;
    private final Queue<BidOfferToSell> bidOffersToSell;

    private int timeToLive;
    private Optional<MutableBidOffer> bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> !bidOffer.isPresent() || bidOffer.get().isExpired();
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new MutableBidOffer(this.items.next(), timeToLive));
    }

    public BidEngine(Items items, Expirable bidOfferExpiration) {
        this.items = items;
        this.bidOfferExpiration = bidOfferExpiration;
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new MutableBidOffer(this.items.next(), timeToLive));
    }

    public BidOffer currentBidOffer() {
        nextBidOfferIfExpired();
        return bidOffer.isPresent() ? bidOffer.get().toBidOffer() : null;
    }

    public BidOffer bid(User user, String name, double value, double increment) throws BidException {
        nextBidOfferIfExpired();
        BidOffer updatedBidOffer = bidOffer.orElseThrow(() -> new BidException(format("current item to bid is not \"%s\"", name)))
                .increment(name, value, increment, user)
                .toBidOffer();
        listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferBidded(updatedBidOffer));
        return updatedBidOffer;
    }

    public void offer(Item item, double initialValue, User user) {
        nextBidOfferIfExpired();
        checkUserOffer(user, item);
        BidOfferToSell bidOfferToSell = checkOffer(item, initialValue);
        bidOffersToSell.offer(bidOfferToSell);
    }

    private BidOfferToSell checkOffer(Item item, double initialValue) {
        if (bidOffer.isPresent() && bidOffer.get().getItem().equals(item)) {
            throw new BidForbiddenException(format("item \"%s\" is the current offer thus can't be offered", item.getName()));
        }
        BidOfferToSell bidOfferToSell = new BidOfferToSell(item, initialValue);
        if (bidOffersToSell.contains(bidOfferToSell)) {
            throw new BidForbiddenException(format("item \"%s\" is already offered", item));
        }
        return bidOfferToSell;
    }

    private void checkUserOffer(User user, Item item) {
        if (!user.equals(item.getOwner())) {
            throw new BidForbiddenException(format("item \"%s\" doesn't belong to user \"%s\"", item, user));
        }
    }


    public void addListener(BidEngineListener bidEngineListener) {
        nextBidOfferIfExpired();
        listeners.add(bidEngineListener);
    }

    public void userIsUnregistered(User user) {
        bidOffer.ifPresent((bidOffer) -> bidOffer.userIsUnregistered(user));
        items.userIsUnregistered(user);
    }

    private void nextBidOfferIfExpired() {
        if (bidOfferExpiration.isExpired()) {
            bidOffer.ifPresent((bidOffer) -> {
                bidOffer.resolve();
                listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferResolved(bidOffer.toBidOffer()));
            });
            bidOffer = nextBidOffer();
            bidOffer.ifPresent((bidOffer) -> listeners.forEach(bidEngineListener -> bidEngineListener.onNewBidOffer(bidOffer.toBidOffer())));
        }
    }

    private Optional<MutableBidOffer> nextBidOffer() {
        if (!bidOffersToSell.isEmpty()) {
            return Optional.of(bidOffersToSell.poll().toBidOffer(timeToLive));
        }
        Item nextItem = items.next();
        if (nextItem == null) {
            return Optional.empty();
        } else {
            return Optional.of(new MutableBidOffer(nextItem, timeToLive));
        }
    }
}
