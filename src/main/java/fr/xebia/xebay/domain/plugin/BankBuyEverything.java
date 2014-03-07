package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOfferToSell;

public class BankBuyEverything extends ActivablePlugin {
    protected BankBuyEverything() {
        super("BankBuyEverything");
    }

    @Override
    protected boolean authorizeIfActivated(BidOfferToSell bidOfferToSell) {
        if (!bidOfferToSell.isInitialValueAtItemPrice()) {
            return true;
        }
        bidOfferToSell.backToBankAtItemPrice();
        return false;
    }
}
