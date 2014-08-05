package fr.xebia.xebay;

import fr.xebia.xebay.domain.Amount;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.MailSender;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;

import java.util.Set;

public enum BidServer {
    BID_SERVER;

    public final Users users;
    public final Items items;
    public final BidEngine bidEngine;
    public final MailSender mailSender;

    BidServer() {
        mailSender = new MailSender();
        if (System.getProperty("xebay.test") != null) {
            users = new Users();
            items = new Items(new Item("category", "an item", new Amount(4.3)));
            bidEngine = new BidEngine(items, () -> false);
            return;
        }
        Set<User> savedUsers = Users.loadUsers();
        users = new Users(savedUsers);

        items = Items.load("items").get();
        bidEngine = new BidEngine(items);
    }
}
