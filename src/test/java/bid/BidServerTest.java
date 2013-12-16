package bid;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BidServerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_not_be_instantiated_without_bid_offer() throws Exception {
        expectedException.expect(BidException.class);

        new BidServer();
    }
}
