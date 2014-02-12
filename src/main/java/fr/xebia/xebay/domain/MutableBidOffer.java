package fr.xebia.xebay.domain;

import java.util.Date;

import static java.lang.Math.max;
import static java.lang.String.format;

class MutableBidOffer {
    private final Item item;
    private final double initialValue;
    private long initialTimeToLive;
    private final long created;

    private double currentValue;
    private User futureBuyer;

    MutableBidOffer(Item item, long initialTimeToLive) {
        this(item, item.getValue(), initialTimeToLive);
    }

    MutableBidOffer(Item item, double initialValue, long initialTimeToLive) {
        this.item = item;
        this.initialValue = initialValue;
        this.initialTimeToLive = initialTimeToLive;
        this.created = new Date().getTime();
        this.currentValue = item.getValue();
    }

    BidOffer toBidOffer() {
        return new BidOffer(item, initialValue, currentValue, getTimeToLive(), futureBuyer);
    }

    User getFutureBuyer() {
        return futureBuyer;
    }

    Item getItem() {
        return item;
    }

    double getInitialValue() {
        return initialValue;
    }

    double getCurrentValue() {
        return currentValue;
    }

    void resolve() {
        if (futureBuyer == null) {
            item.depreciate();
        } else {
            item.concludeTransaction(currentValue, futureBuyer);
        }
        currentValue = item.getValue();
    }

    long getTimeToLive() {
        long millisecondsSinceCreated = new Date().getTime() - created;
        return max(0, initialTimeToLive - millisecondsSinceCreated);
    }

    boolean isExpired() {
        return getTimeToLive() == 0;
    }

    void userIsUnregistered(User user) {
        if (user == futureBuyer) {
            futureBuyer = null;
        }
    }

    MutableBidOffer increment(String name, double value, double increment, User user) throws BidException {
        if (null == user || (null == user.getName())) {
            throw new BidException("bad user");
        }
        if (!item.getName().equals(name)) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        if (currentValue != value) {
            throw new BidException(format("value for \"%s\" is not %s but %s", item.getName(), Double.toString(value), Double.toString(currentValue)));
        }
        if (currentValue / 10 > increment) {
            throw new BidException(format("increment %s is less than ten percent of initial value %s of item \"%s\"", Double.toString(increment), Double.toString(item.getValue()), item.getName()));
        }

        if (!user.canBid(currentValue + increment)) {
            throw new BidException(format("user can't bid %s, not enought money left.", Double.toString(currentValue + increment)));
        }

        return increment(increment, user);
    }

    private MutableBidOffer increment(double increment, User user) {
        this.currentValue += increment;
        this.futureBuyer = user;
        return this;
    }
}