package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;

public class BidEngineSocketOutput {

    BidOffer info;

    String error;

    public BidOffer getInfo() {
        return info;
    }

    public void setInfo(BidOffer info) {
        this.info = info;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
