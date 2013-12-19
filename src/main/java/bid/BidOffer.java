package bid;

class BidOffer {
    private final Item item;

    private double currentValue;
    private User user;

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

    BidOffer increment(double increment, User user) {
        this.currentValue += increment;
        this.user = user;
        return this;
    }

    void resolve() {
        if (user != null) {
            item.updateValue(currentValue);
            user.buy(item);
        }
    }
}
