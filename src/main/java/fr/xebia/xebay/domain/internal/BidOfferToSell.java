package fr.xebia.xebay.domain.internal;

import static fr.xebia.xebay.domain.internal.Item.BANK;

public class BidOfferToSell {
    private final Item item;
    private final double initialValue;

    public BidOfferToSell(Item item, double initialValue) {
        this.item = item;
        this.initialValue = initialValue;
    }

    public BidOffer toBidOffer(int initialTimeToLive) {
        return new BidOffer(item, initialValue, initialTimeToLive);
    }

    public boolean isInitialValueAtItemPrice() {
        return initialValue == item.getValue();
    }

    public void backToBankAtItemPrice() {
        item.concludeTransaction(item.getValue(), BANK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BidOfferToSell that = (BidOfferToSell) o;
        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }
}
