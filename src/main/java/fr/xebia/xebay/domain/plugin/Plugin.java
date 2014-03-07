package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOfferToSell;

public interface Plugin {
    boolean authorize(BidOfferToSell bidOfferToSell);
}
