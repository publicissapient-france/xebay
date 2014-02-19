package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.model.BidOffer;

public interface BidEngineListener {
    void onBidOfferBidded(BidOffer updatedBidOffer);

    void onBidOfferResolved(BidOffer resolvedBidOffer);

    void onNewBidOffer(BidOffer newBidOffer);
}
