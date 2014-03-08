package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.BidOfferToSell;
import fr.xebia.xebay.domain.internal.Items;

public interface Plugin {
    boolean authorize(BidOfferToSell bidOfferToSell);

    void onBidOfferResolved(BidOffer bidOffer, Items items);
}
