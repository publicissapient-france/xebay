package bid;

class BidOffer {
    private final String itemName;
    private final double initialValue;

    private double currentValue;

    BidOffer(String itemName, double initialValue) {
        this.itemName = itemName;
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    String getName() {
        return itemName;
    }

    double getInitialValue() {
        return initialValue;
    }

    double getCurrentValue() {
        return currentValue;
    }

    BidOffer increment(double increment) {
        currentValue += increment;
        return this;
    }
}
