package fr.xebia.xebay.domain;

public interface BidEngineListener {
    void onBidOfferBidded(BidOffer updatedBidOffer, User bidder);

    void onBidOfferResolved(BidOffer resolvedBidOffer, User buyer);

    void onNewBidOffer(BidOffer newBidOffer);
}
