package fr.xebia.xebay.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XebayTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Expirable expiration;

    private Users users = new Users();
    private User user;

    @Before
    public void createUser() {
        user = users.create("email@provider.com");
    }

    @Before
    public void bidOffersNeverExpires() {
        when(expiration.isExpired()).thenReturn(false);
    }

    @Test
    public void server_should_give_a_bid_offer() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.itemName).isEqualTo("an item");
        assertThat(bidOffer.initialValue).isEqualTo(4.3);
        assertThat(bidOffer.ownerEmail.isPresent()).isFalse();
    }

    @Test
    public void when_no_bid_has_occurred_current_value_is_equal_to_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.currentValue).isEqualTo(4.3);
    }

    @Test
    public void a_bid_can_be_done() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
        BidOffer bidOffer = bidEngine.bid(user, "an item", 4.3, 2.1);

        assertThat(bidOffer.currentValue).isEqualTo(6.4);
        assertThat(bidOffer.futureBuyerEmail.get()).isEqualTo("email@provider.com");
    }

    @Test
    public void user_have_to_bid_on_current_item() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("current item to bid is not \"another item\"");

        bidEngine.bid(user, "another item", 4.3, 2.1);
    }

    @Test
    public void user_have_to_bid_on_current_item_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("value for \"an item\" is not 4.1 but 4.3");

        bidEngine.bid(user, "an item", 4.1, 2.1);
    }

    @Test
    public void should_not_bid_with_less_than_ten_percent_of_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("increment 0.42 is less than ten percent of initial value 4.3 of item \"an item\"");

        bidEngine.bid(user, "an item", 4.3, 0.42);
    }

    @Test
    public void a_bid_is_valid_until_bid_offer_expires() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), () -> true);

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.itemName).isEqualTo("another item");
    }

    @Test
    public void when_only_one_user_bids_then_he_wins() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), expiration);
        bidEngine.bid(user, "an item", 4.3, 0.7);

        resolvesBidOffer(bidEngine);

        assertThat(user.getBalance()).isEqualTo(995);
        Item purchasedItem = user.getItems().iterator().next();
        assertThat(purchasedItem.getName()).isEqualTo("an item");
        assertThat(purchasedItem.getValue()).isEqualTo(5.0);
        assertThat(purchasedItem.getOwner()).isEqualTo(user);
    }

    @Test
    public void should_makes_offer() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), expiration);
        bidEngine.bid(user, "an item", 4.3, 0.7);
        resolvesBidOffer(bidEngine);

        bidEngine.offer(user, "an item", 5.9);

        resolvesBidOffer(bidEngine);
        BidOffer bidOffer = bidEngine.currentBidOffer();
        assertThat(bidOffer.itemName).isEqualTo("an item");
        assertThat(bidOffer.ownerEmail.get()).isEqualTo("email@provider.com");
        assertThat(bidOffer.initialValue).isEqualTo(5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_doesnt_belong_to_user() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), expiration);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("item \"another item\" doesn't belong to user \"email@provider.com\"");

        bidEngine.offer(user, "another item", 5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_is_current_offer() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), expiration);
        bidEngine.bid(user, "an item", 4.3, 2.1);
        resolvesBidOffer(bidEngine);
        bidEngine.offer(user, "an item", 5.9);
        resolvesBidOffer(bidEngine);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("item \"an item\" is the current offer thus can't be offered");

        bidEngine.offer(user, "an item", 5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_doesnt_exists() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), expiration);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("item \"inexistant\" doesn't exist");

        bidEngine.offer(user, "inexistant", 5.9);
    }

    @Test
    public void when_a_bid_is_won_seller_increase_money() {
        User seller = users.create("seller@host.com");
        User buyer = users.create("buyer@host.com");
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), expiration);

        // let seller buy "an item"
        bidEngine.bid(seller, "an item", 4.3, 0.7);
        resolvesBidOffer(bidEngine);
        // let seller offer his item
        bidEngine.offer(seller, "an item", 5.0);
        resolvesBidOffer(bidEngine);
        // let buyer buy "an item" owned by seller
        bidEngine.bid(buyer, "an item", 5.0, 0.8);
        resolvesBidOffer(bidEngine);

        assertThat(seller.getItems()).isEmpty();
        assertThat(seller.getBalance()).isEqualTo(1000.8);
    }

    @Test
    public void when_nobody_bid_then_item_value_loose_ten_percent_of_its_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.33)), expiration);

        resolvesBidOffer(bidEngine);

        assertThat(bidEngine.currentBidOffer().initialValue).isEqualTo(3.90);
    }

    @Test
    public void user_cant_bid_without_enough_balance() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 900), new Item("another item", 5)));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("user can't bid 1001.0, not enought money left.");

        bidEngine.bid(user, "an item", 900, 101);
    }

    @Test(expected = BidException.class)
    public void if_user_cant_bid_current_offer_keep_unchanged() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 900), new Item("another item", 5)));
        try {
            bidEngine.bid(user, "an item", 900, 101);
        } finally {
            assertThat(user.getBalance()).isEqualTo(1000);
            assertThat(bidEngine.currentBidOffer().currentValue).isEqualTo(900);
        }
    }

    @Test
    public void should_not_offer_item_that_is_owned_by_a_user() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), expiration);
        bidEngine.bid(user, "an item", 4.3, 2.1);
        resolvesBidOffer(bidEngine);

        BidOffer noNewBidOffer = bidEngine.currentBidOffer();

        assertThat(noNewBidOffer).isNull();
    }

    private void resolvesBidOffer(BidEngine bidEngine) {
        boolean previousExpirationState = expiration.isExpired();
        when(expiration.isExpired()).thenReturn(true);
        bidEngine.currentBidOffer();
        when(expiration.isExpired()).thenReturn(previousExpirationState);
    }
}
