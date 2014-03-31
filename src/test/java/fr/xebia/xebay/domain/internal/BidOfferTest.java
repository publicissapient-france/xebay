package fr.xebia.xebay.domain.internal;

import org.junit.Test;

import java.math.BigDecimal;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class BidOfferTest {
    @Test
    public void should_have_same_value_for_current_and_intial_value_at_startup() {
        BidOffer bidOffer = new BidOffer(null, new BigDecimal(4.3), DEFAULT_TIME_TO_LIVE);

        assertThat(bidOffer.initialValue).isEqualTo(new BigDecimal(4.3));
        assertThat(bidOffer.currentValue).isEqualTo(new BigDecimal(4.3));
    }

    @Test
    public void should_always_bid_with_ten_percent_of_initial_value() {
        String itemName = "an item";
        User user = new User("key", "email@provider.net");
        BidOffer offer = new BidOffer(new Item("category", itemName, new BigDecimal(4)), DEFAULT_TIME_TO_LIVE);
        offer.bid(itemName, new BigDecimal(4.4), user);

        offer.bid(itemName, new BigDecimal(4.8), user);
    }
}
