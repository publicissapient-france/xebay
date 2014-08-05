package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.internal.AdminUser;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BidEngineTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_not_be_instantiated_without_items() throws Exception {
        expectedException.expect(BidException.class);

        new BidEngine(new Items());
    }

    @Test
    public void admin_should_not_bid() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("category", "an item", new Amount(4.3))));
        expectedException.expect(BidException.class);
        expectedException.expectMessage("admin is not authorized to bid");

        bidEngine.bid(new AdminUser(), "an item", 4.3 + 1.2);
    }
}
