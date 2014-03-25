package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static fr.xebia.xebay.domain.utils.Math.round;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XebayTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Expirable expiration;

    private Users users;
    private User user;

    @Before
    public void createUsersAndUser() {
        users = new Users();
        user = users.create("user1");
    }

    @Before
    public void bidOffersNeverExpires() {
        when(expiration.isExpired()).thenReturn(false);
    }

    @Test
    public void server_should_give_a_bid_offer() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))));

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getItem().getCategory()).isEqualTo("category");
        assertThat(bidOffer.getItem().getValue()).isEqualTo(4.3);
        assertThat(bidOffer.getBidder()).isNull();
        assertThat(bidOffer.isExpired()).isFalse();
        assertThat(bidOffer.getTimeToLive()).isLessThanOrEqualTo(DEFAULT_TIME_TO_LIVE);
    }

    @Test
    public void a_bid_can_be_done() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))));
        BidOffer bidOffer = bidEngine.bid(user, "an item", 4.3 + 2.1);

        assertThat(bidOffer.getItem().getValue()).isEqualTo(6.4);
        assertThat(bidOffer.getBidder()).isNotNull().isEqualTo("user1");
    }

    @Test
    public void user_have_to_bid_on_current_item() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("current item to bid is not \"another item\"");

        bidEngine.bid(user, "another item", 4.3 + 2.1);
    }

    @Test
    public void should_not_bid_with_less_than_ten_percent_of_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("increment 0.42$ is less than ten percent of initial value 4.30$ of item \"an item\"");

        bidEngine.bid(user, "an item", 4.3 + 0.42);
    }

    @Test
    public void should_bid_if_exactly_ten_percent_increment_of_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(38.07))));
        BidOffer bidOffer = bidEngine.bid(user, "an item", 38.07 + 3.807);

        assertThat(bidOffer.getItem().getValue()).isEqualTo(41.88);
    }

    @Test
    public void a_bid_is_valid_until_bid_offer_expires() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3)), new Item("category", "another item", new BigDecimal(2.4))), () -> true);

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.getItem().getName()).isEqualTo("another item");
    }

    @Test
    public void when_only_one_user_bids_then_he_wins() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3)), new Item("category", "another item", new BigDecimal(2.4))), expiration);
        bidEngine.bid(user, "an item", 4.3 + 0.7);

        resolvesBidOffer(bidEngine);

        assertThat(user.getBalance()).isEqualTo(new BigDecimal(995));
        Item purchasedItem = user.getItems().iterator().next();
        assertThat(purchasedItem.getName()).isEqualTo("an item");
        assertThat(purchasedItem.getValue()).isEqualTo(new BigDecimal(5));
        assertThat(purchasedItem.getOwner()).isEqualTo(user);
    }

    @Test
    public void should_makes_offer() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        Item anotherItem = new Item("category", "another item", new BigDecimal(2.4));
        BidEngine bidEngine = new BidEngine(new Items(anItem, anotherItem), expiration);
        bidEngine.bid(user, "an item", 4.3 + 0.7);
        resolvesBidOffer(bidEngine);

        bidEngine.offer(user, anItem, 5.9);

        resolvesBidOffer(bidEngine);
        BidOffer bidOffer = bidEngine.currentBidOffer();
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getOwner()).isNotNull().isEqualTo("user1");
        assertThat(bidOffer.getBidder()).isNull();
        assertThat(bidOffer.getItem().getValue()).isEqualTo(5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_doesnt_belong_to_user() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        Item anotherItem = new Item("category", "another item", new BigDecimal(2.4));
        BidEngine bidEngine = new BidEngine(new Items(anItem, anotherItem), expiration);
        expectedException.expect(BidForbiddenException.class);
        expectedException.expectMessage("item \"another item\" doesn't belong to user \"user1\"");

        bidEngine.offer(user, anotherItem, 5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_is_current_offer() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        Item anotherItem = new Item("category", "another item", new BigDecimal(2.4));

        BidEngine bidEngine = new BidEngine(new Items(anItem, anotherItem), expiration);
        bidEngine.bid(user, "an item", 4.3 + 2.1);
        resolvesBidOffer(bidEngine);
        bidEngine.offer(user, anItem, 5.9);
        resolvesBidOffer(bidEngine);
        expectedException.expect(BidForbiddenException.class);
        expectedException.expectMessage("item \"an item\" is the current offer thus can't be offered");

        bidEngine.offer(user, anItem, 6.0);
    }

    @Test
    public void should_not_makes_offer_if_item_is_null() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))), expiration);
        expectedException.expect(NullPointerException.class);
        bidEngine.offer(user, null, 5.9);
    }

    @Test
    public void should_not_makes_offer_if_user_is_null() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))), expiration);
        expectedException.expect(NullPointerException.class);

        bidEngine.offer(null, new Item("category", "an item", new BigDecimal(4.3)), 5.9);
    }

    @Test
    public void should_not_makes_offer_if_item_is_already_offered() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        Item anotherItem = new Item("category", "another item", new BigDecimal(2.4));

        BidEngine bidEngine = new BidEngine(new Items(anItem, anotherItem), expiration);

        bidEngine.bid(user, "an item", 4.3 + 2.1);
        resolvesBidOffer(bidEngine);

        bidEngine.offer(user, anItem, 5.9);
        expectedException.expect(BidForbiddenException.class);
        expectedException.expectMessage("item \"an item\" is already offered");

        bidEngine.offer(user, anItem, 6.5);
    }

    @Test
    public void should_flag_items_on_sale() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        BidEngine bidEngine = new BidEngine(new Items(anItem), expiration);
        bidEngine.bid(user, anItem.getName(), 6);
        resolvesBidOffer(bidEngine);

        bidEngine.offer(user, anItem, 7);
        resolvesBidOffer(bidEngine);

        assertThat(anItem.isOffered()).isTrue();

        bidEngine.bid(user, anItem.getName(), 8);
        resolvesBidOffer(bidEngine);

        assertThat(anItem.isOffered()).isFalse();
    }

    @Test
    public void a_user_can_buy_an_item_offered_by_another() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));
        BidEngine bidEngine = new BidEngine(new Items(anItem), expiration);
        User seller = users.create("seller");
        User buyer = users.create("buyer");

        //seller buys the item
        BidOffer bidOffer = bidEngine.bid(seller, "an item", 4.3 + 0.7);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getItem().getValue()).isEqualTo(5.0);
        assertThat(bidOffer.getBidder()).isNotNull().isEqualTo("seller");

        resolvesBidOffer(bidEngine);
        assertThat(anItem.getOwner().getName()).isNotNull().isEqualTo("seller");

        //seller offers the item
        bidEngine.offer(seller, anItem, 5.9);
        resolvesBidOffer(bidEngine);

        bidOffer = bidEngine.currentBidOffer();
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getOwner()).isNotNull().isEqualTo("seller");
        assertThat(bidOffer.getBidder()).isNull();
        assertThat(bidOffer.getItem().getValue()).isEqualTo(5.9);

        //buyer buys the item
        bidOffer = bidEngine.bid(buyer, "an item", 5.9 + 0.6);

        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getItem().getValue()).isEqualTo(6.5);
        assertThat(bidOffer.getOwner()).isNotNull().isEqualTo("seller");
        assertThat(bidOffer.getBidder()).isNotNull().isEqualTo("buyer");

        resolvesBidOffer(bidEngine);

        assertThat(anItem.getOwner().getName()).isNotNull().isEqualTo("buyer");
    }

    @Test
    public void when_a_bid_is_won_seller_increase_money() {
        Item anItem = new Item("category", "an item", new BigDecimal(4.3));

        User seller = users.create("seller");
        User buyer = users.create("buyer");
        BidEngine bidEngine = new BidEngine(new Items(anItem), expiration);

        // let seller buy "an item"
        bidEngine.bid(seller, "an item", 4.3 + 0.7);
        resolvesBidOffer(bidEngine);
        // let seller offer his item
        bidEngine.offer(seller, anItem, 5.0);
        resolvesBidOffer(bidEngine);
        // let buyer buy "an item" owned by seller
        bidEngine.bid(buyer, "an item", 5.0 + 0.8);
        resolvesBidOffer(bidEngine);

        assertThat(seller.getItems()).isEmpty();
        assertThat(round(seller.getBalance())).isEqualTo(1000.8);
    }

    @Test
    public void when_nobody_bid_then_item_value_loose_ten_percent_of_its_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.33))), expiration);

        resolvesBidOffer(bidEngine);

        assertThat(bidEngine.currentBidOffer().getItem().getValue()).isEqualTo(3.90);
    }

    @Test
    public void user_cant_bid_without_enough_balance() {
        BigDecimal initialItemValue = User.INITIAL_BALANCE.subtract(new BigDecimal(100));
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", initialItemValue), new Item("category", "another item", new BigDecimal(5))));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("user can't bid 1001.00$, not enought money left.");

        bidEngine.bid(user, "an item", 1001);
    }

    @Test(expected = BidException.class)
    public void if_user_cant_bid_current_offer_keep_unchanged() {
        BigDecimal initialItemValue = User.INITIAL_BALANCE.subtract(new BigDecimal(100));
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", initialItemValue), new Item("category", "another item", new BigDecimal(5))));
        try {
            bidEngine.bid(user, "an item", 101);
        } finally {
            assertThat(user.getBalance()).isEqualTo(User.INITIAL_BALANCE);
            assertThat(bidEngine.currentBidOffer().getItem().getValue()).isEqualTo(900);
        }
    }

    @Test
    public void should_not_offer_item_that_is_owned_by_a_user() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new BigDecimal(4.3))), expiration);
        bidEngine.bid(user, "an item", 4.3 + 2.1);
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
