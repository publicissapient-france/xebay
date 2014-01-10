package bid;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

public class BidTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private String key;
    private Users users = new Users();

    @Before
    public void setUp() throws Exception {
        User user = users.create("email@provider.com");
        key = user.getKey();
    }

    @Test
    public void server_should_give_a_bid_offer() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getItem().getValue()).isEqualTo(4.3);
    }

    @Test
    public void when_no_bid_has_occurred_current_value_is_equal_to_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);

        BidOffer bidOffer = bidEngine.currentBidOffer();

        assertThat(bidOffer.getCurrentValue()).isEqualTo(4.3);
    }

    @Test
    public void a_bid_can_be_done() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
        BidOffer bidOffer = bidEngine.bid(key, "an item", 4.3, 2.1);

        assertThat(bidOffer.getCurrentValue()).isEqualTo(6.4);
    }

    @Test
    public void user_have_to_bid_on_current_item() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("current item to bid is not \"another item\"");

        bidEngine.bid(key, "another item", 4.3, 2.1);
    }

    @Test
    public void user_have_to_bid_on_current_item_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("value for \"an item\" is not 4.1 but 4.3");

        bidEngine.bid(key, "an item", 4.1, 2.1);
    }

    @Test
    public void should_not_bid_with_less_than_ten_percent_of_initial_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
        expectedException.expect(BidException.class);
        expectedException.expectMessage("increment 0.42 is less than ten percent of initial value 4.3 of item \"an item\"");

        bidEngine.bid(key, "an item", 4.3, 0.42);
    }

    @Test
    public void a_bid_is_valid_until_ten_tick() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), users);

        range(0, 10).forEach((i) -> bidEngine.tick());

        assertThat(bidEngine.currentBidOffer().getItem().getName()).isEqualTo("another item");
    }

    @Test
    public void when_only_one_user_bids_then_he_wins() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3), new Item("another item", 2.4)), users);
        bidEngine.bid(key, "an item", 4.3, 0.7);

        range(0, 10).forEach((i) -> bidEngine.tick());

        User user = users.getUser(key);
        assertThat(user.getBalance()).isEqualTo(995);
        Item purchasedItem = user.getItems().iterator().next();
        assertThat(purchasedItem.getName()).isEqualTo("an item");
        assertThat(purchasedItem.getValue()).isEqualTo(5.0);
        assertThat(purchasedItem.getOwner()).isEqualTo(user);
    }

    @Test
    public void when_a_bid_is_won_seller_increase_money() {
        String sellerKey = users.create("seller@host.com").getKey();
        String buyerKey = users.create("buyer@host.com").getKey();

        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);

        bidEngine.bid(sellerKey, "an item", 4.3, 0.7);
        range(0, 10).forEach((i) -> bidEngine.tick());

        bidEngine.bid(buyerKey, "an item", 5.0, 0.8);

        range(0, 10).forEach((i) -> bidEngine.tick());

        User user = users.getUser(sellerKey);
        assertThat(user.getItems()).isEmpty();
        assertThat(user.getBalance()).isEqualTo(1000.8);
    }

    @Test
    public void when_nobody_bid_then_item_value_loose_ten_percent_of_its_value() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.33), new Item("another item", 2.4)), users);
        Item item = bidEngine.currentBidOffer().getItem();

        range(0, 10).forEach((i) -> bidEngine.tick());

        assertThat(item.getValue()).isEqualTo(3.90);
    }
}
