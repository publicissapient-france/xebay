package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.internal.MutableBidOffer;
import org.junit.Test;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class MutableBidOfferTest {
    @Test
    public void should_have_same_value_for_current_and_intial_value_at_startup() {
        MutableBidOffer mutableBidOffer = new MutableBidOffer(null, 4.3, DEFAULT_TIME_TO_LIVE);

        assertThat(mutableBidOffer.initialValue).isEqualTo(4.3);
        assertThat(mutableBidOffer.currentValue).isEqualTo(4.3);
    }
}
