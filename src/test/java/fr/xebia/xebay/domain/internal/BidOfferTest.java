package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.Amount;
import org.junit.Test;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class BidOfferTest {
    @Test
    public void should_have_same_value_for_current_and_intial_value_at_startup() {
        BidOffer bidOffer = new BidOffer(null, new Amount(4.3), DEFAULT_TIME_TO_LIVE);

        assertThat(bidOffer.initialValue).isEqualTo(new Amount(4.3));
        assertThat(bidOffer.currentValue).isEqualTo(new Amount(4.3));
    }

    @Test
    public void should_always_bid_with_ten_percent_of_initial_value() {
        String itemName = "an item";
        User user = new User("key", "email@provider.net");
        BidOffer offer = new BidOffer(new Item("category", itemName, new Amount(4)), DEFAULT_TIME_TO_LIVE);
        offer.bid(itemName, new Amount(4.4), user);

        offer.bid(itemName, new Amount(4.8), user);
    }

    @Test
    public void should_bid_with_ten_percent_of_initial_value_but_round() {
        String itemName = "an item";
        User user = new User("key", "email@provider.net");
        BidOffer offer = new BidOffer(new Item("category", itemName, new Amount(43.173)), DEFAULT_TIME_TO_LIVE);

        offer.bid(itemName, new Amount(47.486), user);
    }
}
