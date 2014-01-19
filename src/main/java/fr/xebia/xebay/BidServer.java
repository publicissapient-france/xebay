package fr.xebia.xebay;

import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.Items;

public enum BidServer {
    BID_SERVER;

    private BidEngine bidEngine;

    BidServer() {
        this.bidEngine = new BidEngine(Items.load("items").get());
    }

    public BidEngine bidEngine() {
        return bidEngine;
    }
}
