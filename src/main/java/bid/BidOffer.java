package bid;

import static java.lang.String.format;

class BidOffer {
    private final Item item;

    private double currentValue;
    private User buyer;

    BidOffer(Item item) {
        this.item = item;
        this.currentValue = item.getValue();
    }

    Item getItem() {
        return item;
    }

    double getCurrentValue() {
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
}
