package bid.api.rest;

import bid.BidOffer;

public class BidOfferParam {

    private BidOffer bidOffer;
    private Double increment;

    public BidOfferParam() {
    }

    public BidOfferParam(BidOffer bidOffer, Double increment) {
        this.bidOffer = bidOffer;
        this.increment = increment;
    }

    public String getName() {
        return bidOffer.getItem().getName();
    }

    public double getCurValue() {
        return bidOffer.getCurrentValue();
    }

    public Double getIncrement() {
        return increment;
    }
}
