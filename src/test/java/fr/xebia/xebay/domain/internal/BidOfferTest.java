package fr.xebia.xebay.domain.internal;

import org.junit.Test;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class BidOfferTest {
    @Test
    public void should_have_same_value_for_current_and_intial_value_at_startup() {
        BidOffer bidOffer = new BidOffer(null, 4.3, DEFAULT_TIME_TO_LIVE);

        assertThat(bidOffer.initialValue).isEqualTo(4.3);
        assertThat(bidOffer.currentValue).isEqualTo(4.3);
    }
}
