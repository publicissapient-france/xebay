package fr.xebia.xebay.domain;

public interface BidEngineListener {

    void onBidOffer(BidOffer bidOffer);

    void onInfo(String message);

}
