package bid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

public class BidTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void server_should_give_a_bid_offer() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.currentBidOffer(key);

        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getItem().getValue()).isEqualTo(4.3);
    }

    @Test
    public void when_no_bid_has_occured_current_value_is_equal_to_initial_value() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.currentBidOffer(key);

        assertThat(bidOffer.getCurrentValue()).isEqualTo(4.3);
    }

    @Test
    public void a_bid_can_be_done() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.bid(key, "an item", 4.3, 2.1);

        assertThat(bidOffer.getCurrentValue()).isEqualTo(6.4);
    }

    @Test
    public void user_have_to_bid_on_current_item() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("current item to bid is not \"another item\"");

        bidServer.bid(key, "another item", 4.3, 2.1);
    }

    @Test
    public void user_have_to_bid_on_current_item_value() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("value for \"an item\" is not 4.1 but 4.3");

        bidServer.bid(key, "an item", 4.1, 2.1);
    }

    @Test
    public void should_not_bid_with_less_than_ten_percent_of_initial_value() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("increment 0.42 is less than ten percent of initial value 4.3 of item \"an item\"");

        bidServer.bid(key, "an item", 4.3, 0.42);
    }

    @Test
    public void a_bid_is_valid_until_ten_tick() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3), new Item("another item", 2.4));
        String key = bidServer.register("email@provider.com");

        range(0, 10).forEach((i) -> bidServer.tick());

        assertThat(bidServer.currentBidOffer(key).getItem().getName()).isEqualTo("another item");
    }

    @Test
    public void when_only_one_user_bids_then_he_wins() {
        BidServer bidServer = new BidServer(new Item("an item", 4.3), new Item("another item", 2.4));
        String key = bidServer.register("email@provider.com");
        bidServer.bid(key, "an item", 4.3, 0.7);

        range(0, 10).forEach((i) -> bidServer.tick());

        User user = bidServer.user(key);
        assertThat(user.getBalance()).isEqualTo(995);
        Item purchasedItem = user.getItems().iterator().next();
        assertThat(purchasedItem.getName()).isEqualTo("an item");
        assertThat(purchasedItem.getValue()).isEqualTo(5.0);
    }

    @Test
    public void when_nobody_bid_then_item_value_loose_ten_percent_of_his_value() {
        BidServer bidServer = new BidServer(new Item("an item", 4.33), new Item("another item", 2.4));
        String key = bidServer.register("email@provider.com");
        Item item = bidServer.currentBidOffer(key).getItem();

        range(0, 10).forEach((i) -> bidServer.tick());

        assertThat(item.getValue()).isEqualTo(3.90);
    }
}
