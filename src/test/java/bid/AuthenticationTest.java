package bid;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationTest {
    @Rule
    public ExpectedException excpectedException = ExpectedException.none();

    private Item bidItem;

    @Before
    public void createBidOffer() {
        bidItem = new Item("an item", 4.3);
    }

    @Test
    public void a_key_is_emmitted_when_register() {
        BidServer bidServer = new BidServer(bidItem);

        String key = bidServer.register("an-email@provider.com");

        assertThat(key).matches(Pattern.compile("[\\w\\-_]+")).hasSize(16);
    }

    @Test
    public void can_not_register_twice() {
        BidServer bidServer = new BidServer(bidItem);
        bidServer.register("an-email@provider.com");
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("\"an-email@provider.com\" is already registered");

        bidServer.register("an-email@provider.com");
    }

    @Test
    public void once_registered_key_allow_user_to_interact_with_server() {
        BidServer bidServer = new BidServer(bidItem);
        String key = bidServer.register("an-email@provider.com");

        BidOffer bidOffer = bidServer.currentBidOffer(key);

        assertThat(bidOffer.getItem()).isEqualTo(bidItem);
    }

    @Test
    public void cant_get_current_bid_offer_if_key_is_not_correct() {
        BidServer bidServer = new BidServer(bidItem);
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("key \"fake key\" is unknown");

        bidServer.currentBidOffer("fake key");
    }

    @Test
    public void cant_bid_if_key_is_not_correct() {
        BidServer bidServer = new BidServer(bidItem);
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("key \"fake key\" is unknown");

        bidServer.bid("fake key", null, 0, 0);
    }
}
