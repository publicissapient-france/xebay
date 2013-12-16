package bid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class BidTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void server_should_give_a_bid_offer() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.currentBidOffer(key);

        assertThat(bidOffer.getName()).isEqualTo("an item");
        assertThat(bidOffer.getInitialValue()).isEqualTo(4.3);
    }

    @Test
    public void when_no_bid_has_occured_current_value_is_equal_to_initial_value() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.currentBidOffer(key);

        assertThat(bidOffer.getCurrentValue()).isEqualTo(4.3);
    }

    @Test
    public void a_bid_can_be_done() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");

        BidOffer bidOffer = bidServer.bid(key, "an item", 4.3, 2.1);

        assertThat(bidOffer.getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(6.4);
    }

    @Test
    public void user_have_to_bid_with_current_item() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("current item to bid is not \"another item\"");

        bidServer.bid(key, "another item", 4.3, 2.1);
    }

    @Test
    public void user_have_to_bid_with_current_item_value() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("value for \"an item\" is not 4.1 but 4.3");

        bidServer.bid(key, "an item", 4.1, 2.1);
    }

    @Test
    public void should_not_bid_with_less_than_ten_percent_of_initial_value() {
        BidServer bidServer = new BidServer(new BidOffer("an item", 4.3));
        String key = bidServer.register("email@provider.com");
        expectedException.expect(BidException.class);
        expectedException.expectMessage("increment 0.42 is less than ten percent of initial value 4.3 of item \"an item\"");

        bidServer.bid(key, "an item", 4.3, 0.42);
    }
}
