package fr.xebia.xebay.domain;

public interface BidEngineListener {
    void onBidOfferBidded(BidOffer updatedBidOffer);

    void onBidOfferResolved(BidOffer resolvedBidOffer);

    void onNewBidOffer(BidOffer newBidOffer);
}
