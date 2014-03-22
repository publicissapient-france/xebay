package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.Expirable;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PluginsTest {
    @Mock
    private Expirable expiration;

    private User user;

    @Before
    public void createUsersAndUser() {
        Users users = new Users();
        user = users.create("user1");
    }

    @Before
    public void bidOffersNeverExpires() {
        when(expiration.isExpired()).thenReturn(false);
    }

    @Test
    public void plugin_bank_buy_everything() {
        Item anItem = new Item("category", "an item", 4.3);
        Item anotherItem = new Item("category", "another item", 2.4);
        BidEngine bidEngine = new BidEngine(new Items(anItem, anotherItem), expiration);
        bidEngine.activate("BankBuyEverything");
        bidEngine.bid(user, "an item", 5.0);
        resolvesBidOffer(bidEngine);

        bidEngine.offer(user, anItem, 5.0);

        assertThat(user.getBalance()).isEqualTo(1000);
        assertThat(user.getItems()).isEmpty();
        resolvesBidOffer(bidEngine);
        BidOffer bidOffer = bidEngine.currentBidOffer();
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getBidder()).isNull();
        assertThat(bidOffer.getItem().getValue()).isEqualTo(5);
    }

    @Test
    public void plugin_all_items_in_category_on_activation() {
        Item anItem = new Item("category", "an item", 4.3);
        BidEngine bidEngine = new BidEngine(new Items(anItem), expiration);
        bidEngine.bid(user, "an item", 5.0);
        resolvesBidOffer(bidEngine);

        bidEngine.activate("AllItemsInCategory");

        assertThat(user.getBalance()).isEqualTo(1495);
    }

    @Test
    public void plugin_all_items_in_category_on_bid_offer_resolved() {
        Item anItem = new Item("category", "an item", 4.3);
        BidEngine bidEngine = new BidEngine(new Items(anItem), expiration);
        bidEngine.activate("AllItemsInCategory");
        bidEngine.bid(user, "an item", 5.0);

        resolvesBidOffer(bidEngine);

        assertThat(user.getBalance()).isEqualTo(1495);
    }

    private void resolvesBidOffer(BidEngine bidEngine) {
        boolean previousExpirationState = expiration.isExpired();
        when(expiration.isExpired()).thenReturn(true);
        bidEngine.currentBidOffer();
        when(expiration.isExpired()).thenReturn(previousExpirationState);
    }
}
