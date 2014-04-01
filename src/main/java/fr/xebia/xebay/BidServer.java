package fr.xebia.xebay;

import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.MailSender;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.Users;

import java.math.BigDecimal;

public enum BidServer {
    BID_SERVER;

    public final Users users;
    public final Items items;
    public final BidEngine bidEngine;
    public final MailSender mailSender;

    BidServer() {
        users = new Users();
        mailSender = new MailSender();
        if (System.getProperty("xebay.test") != null) {
            items = new Items(new Item("category", "an item", new BigDecimal(4.3)));
            bidEngine = new BidEngine(items, () -> false);
            return;
        }

        items = Items.load("items").get();
        bidEngine = new BidEngine(items);
    }
}
