package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;

public class BidEngineSocketOutput {

    BidOffer bidOffer;

    String info;

    String error;

    public BidOffer getBidOffer() {
        return bidOffer;
    }

    public void setBidOffer(BidOffer bidOffer) {
        this.bidOffer = bidOffer;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
