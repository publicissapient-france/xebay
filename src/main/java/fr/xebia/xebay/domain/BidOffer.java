package fr.xebia.xebay.domain;

import java.util.logging.Logger;

import static java.lang.String.format;

public class BidOffer {
    private static final Logger log = Logger.getLogger("BidOffer");

    private Item item;

    private double currentValue;
    private User buyer;
    private int timeToLive;

    public BidOffer() {
    }

    public BidOffer(Item item) {
        this.item = item;
        this.currentValue = item.getValue();
    }

    public User getBuyer() {
        return buyer;
    }


    public Item getItem() {
        return item;
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
        if((null == user) || (null == user.getEmail())){
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

        return increment(increment, user);
    }

    private BidOffer increment(double increment, User user) {
        this.currentValue += increment;
        this.buyer = user;
        return this;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public String toString() {
        return "BidOffer{" +
                "item=" + item.getName() +
                ", currentValue=" + currentValue +
                ", buyer=" + (buyer != null ? buyer.getEmail() : "") +
                ", timeToLive=" + timeToLive +
                '}';
    }

    //    public String getItemName() {
//        return item.getName();
//    }
//    }
}
