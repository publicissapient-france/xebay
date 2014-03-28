package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.dto.BidDemand;

public class BidEngineSocketInput {

    BidDemand bid;

    BidDemand offer;

    public BidDemand getOffer() {
        return offer;
    }

    public void setOffer(BidDemand offer) {
        this.offer = offer;
    }

    public BidDemand getBid() {
        return bid;
    }

    public void setBid(BidDemand bid) {
        this.bid = bid;
    }
}
