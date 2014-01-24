package fr.xebia.xebay.domain;

import java.util.Date;

import static java.lang.Math.max;
import static java.lang.String.format;

public class BidOffer {
    private final Item item;
    private final double initialValue;
    private long initialTimeToLive;
    private final long created;

    private double currentValue;
    private User buyer;

    public BidOffer(Item item, long initialTimeToLive) {
        this(item, item.getValue(), initialTimeToLive);
    }

    public BidOffer(Item item, double initialValue, long initialTimeToLive) {
        this.item = item;
        this.initialValue = initialValue;
        this.initialTimeToLive = initialTimeToLive;
        this.created = new Date().getTime();
        this.currentValue = item.getValue();
    }

    public User getBuyer() {
        return buyer;
    }

    public Item getItem() {
        return item;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    void resolve() {
        if (buyer == null) {
            item.depreciate();
        } else {
            item.concludeTransaction(currentValue, buyer);
        }
    }

    BidOffer increment(String name, double value, double increment, User user) throws BidException {
        if (null == user || (null == user.getEmail())) {
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

    private BidOffer increment(double increment, User user) {
        this.currentValue += increment;
        this.buyer = user;
        return this;
    }

    public long getTimeToLive() {
        long millisecondsSinceCreated = new Date().getTime() - created;
        return max(0, initialTimeToLive - millisecondsSinceCreated);
    }

    boolean isExpired() {
        return getTimeToLive() == 0;
    }
}