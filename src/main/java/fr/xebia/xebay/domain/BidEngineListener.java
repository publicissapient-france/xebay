package fr.xebia.xebay.domain;

public interface BidEngineListener {

    void onBidOfferStarted(BidOffer newBidOffer);
    
    void onBidOfferUpdated(BidOffer updatedBidOffer);

    void onBidOfferResolved(BidOffer resolvedBidOffer);
}
