package fr.xebia.xebay.domain.internal;

public class BidOfferToSell {
    private final Item item;
    private final double initialValue;

    public BidOfferToSell(Item item, double initialValue) {
        this.item = item;
        this.initialValue = initialValue;
    }

    public MutableBidOffer toBidOffer(int initialTimeToLive) {
        return new MutableBidOffer(item, initialValue, initialTimeToLive);
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
