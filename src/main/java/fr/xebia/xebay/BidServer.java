package fr.xebia.xebay;

import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.Items;

public enum BidServer {
    BID_SERVER;

    private final BidEngine bidEngine;

    BidServer() {
        if (System.getProperty("xebay.test") != null) {
            bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), () -> false);
            return;
        }
        bidEngine = new BidEngine(Items.load("items").get());
    }

    public BidEngine bidEngine() {
        return bidEngine;
    }
}
