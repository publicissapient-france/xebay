package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.internal.*;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.plugin.Plugins;

import java.util.*;

import static java.lang.String.format;

public class BidEngine {
    public static final int DEFAULT_TIME_TO_LIVE = 10000;

    private final List<BidEngineListener> listeners = new ArrayList<>();
    private final Items items;
    private final Expirable bidOfferExpiration;
    private final Queue<BidOfferToSell> bidOffersToSell;
    private final Plugins plugins;

    private int timeToLive;
    private Optional<BidOffer> bidOffer;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOfferExpiration = () -> !bidOffer.isPresent() || bidOffer.get().isExpired();
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new BidOffer(this.items.next(), timeToLive));
        this.plugins = new Plugins();
    }

    public BidEngine(Items items, Expirable bidOfferExpiration) {
        this.items = items;
        this.bidOfferExpiration = bidOfferExpiration;
        this.bidOffersToSell = new ArrayDeque<>();
        this.timeToLive = DEFAULT_TIME_TO_LIVE;
        this.bidOffer = Optional.of(new BidOffer(this.items.next(), timeToLive));
        this.plugins = new Plugins();
    }

    public fr.xebia.xebay.domain.BidOffer currentBidOffer() {
        nextBidOfferIfExpired();
        return bidOffer.isPresent() ? bidOffer.get().toBidOffer(bidOfferExpiration.isExpired()) : null;
    }

    public fr.xebia.xebay.domain.BidOffer bid(User user, String itemName, double newValue) throws BidException {
        if (user.isInRole(AdminUser.ADMIN_ROLE)) {
            throw new BidException("admin is not authorized to bid");
        }
        nextBidOfferIfExpired();
        fr.xebia.xebay.domain.BidOffer updatedBidOffer = bidOffer
                .orElseThrow(() -> new BidException(format("current item to bid is not \"%s\"", itemName)))
                .bid(itemName, newValue, user)
                .toBidOffer(bidOfferExpiration.isExpired());
        listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferUpdated(updatedBidOffer));
        return updatedBidOffer;
    }

    public void activate(String pluginName) {
        plugins.activate(pluginName);
    }

    public void deactivate(String pluginName) {
        plugins.deactivate(pluginName);
    }

    public void offer(User user, Item item, double initialValue) {
        nextBidOfferIfExpired();
        checkUserOffer(user, item);
        BidOfferToSell bidOfferToSell = checkOffer(item, initialValue);
        if (plugins.authorize(bidOfferToSell)) {
            bidOffersToSell.offer(bidOfferToSell);
        }
    }

    private BidOfferToSell checkOffer(Item item, double initialValue) {
        if (bidOffer.isPresent() && bidOffer.get().item.equals(item)) {
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
                listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferResolved(bidOffer.toBidOffer(true)));
            });
            bidOffer = nextBidOffer();
            bidOffer.ifPresent((bidOffer) -> listeners.forEach(bidEngineListener -> bidEngineListener.onBidOfferStarted(bidOffer.toBidOffer(true))));
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
