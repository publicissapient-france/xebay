package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.BidException;

import java.util.Date;

import static fr.xebia.xebay.domain.internal.Item.BANK;
import static java.lang.Math.max;
import static java.lang.String.format;

public class BidOffer {
    private static final double MIN_BID_RATIO = 0.1;
    public final Item item;
    public final double initialValue;
    private final long created;
    private final long initialTimeToLive;
    public double currentValue;
    private User futureBuyer;

    public BidOffer(Item item, long initialTimeToLive) {
        this(item, item.getValue(), initialTimeToLive);
    }

    public BidOffer(Item item, double initialValue, long initialTimeToLive) {
        this.item = item;
        this.initialValue = initialValue;
        this.initialTimeToLive = initialTimeToLive;
        this.created = new Date().getTime();
        this.currentValue = initialValue;
    }

    public fr.xebia.xebay.domain.BidOffer toBidOffer(boolean isExpired) {
        return new fr.xebia.xebay.domain.BidOffer(item.getCategory(),
                item.getName(),
                currentValue,
                getTimeToLive(),
                futureBuyer == null ? (item.getOwner() == BANK ? null : item.getOwner().getName()) : futureBuyer.getName(),
                isExpired);
    }

    public void resolve() {
        if (futureBuyer == null) {
            item.depreciate();
        } else {
            item.concludeTransaction(currentValue, futureBuyer);
        }
        currentValue = item.getValue();
    }

    public long getTimeToLive() {
        long millisecondsSinceCreated = new Date().getTime() - created;
        return max(0, initialTimeToLive - millisecondsSinceCreated);
    }

    public boolean isExpired() {
        return getTimeToLive() == 0;
    }

    public void userIsUnregistered(User user) {
        if (user == futureBuyer) {
            futureBuyer = null;
        }
    }

    public BidOffer bid(String name, double newValue, User user) throws BidException {
        checkUser(user);
        if (!item.getName().equals(name)) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        double increment = exactCalcul(newValue - currentValue);
        if (exactCalcul(currentValue * MIN_BID_RATIO) > increment) {
            throw new BidException(format("increment %s is less than ten percent of initial value %s of item \"%s\"", Double.toString(increment), Double.toString(item.getValue()), item.getName()));
        }

        if (!user.canBid(currentValue + increment)) {
            throw new BidException(format("user can't bid %s, not enought money left.", Double.toString(currentValue + increment)));
        }

        return increment(increment, user);
    }

    private double exactCalcul(double value) {
        return Math.round(value * 100000) / 100000.0;
    }

    private void checkUser(User user) {
        if (null == user || (null == user.getName())) {
            throw new BidException("bad user");
        }
    }

    private BidOffer increment(double increment, User user) {
        this.currentValue += increment;
        this.futureBuyer = user;
        return this;
    }
}