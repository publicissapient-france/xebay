package fr.xebia.xebay;

import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.Items;
import fr.xebia.xebay.domain.Users;

public enum BidServer {
    BID_SERVER;

    public final Users users;
    public final BidEngine bidEngine;

    BidServer() {
        users = new Users();
        if (System.getProperty("xebay.test") != null) {
            bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), () -> false);
            return;
        }
        bidEngine = new BidEngine(Items.load("items").get());
    }
}
