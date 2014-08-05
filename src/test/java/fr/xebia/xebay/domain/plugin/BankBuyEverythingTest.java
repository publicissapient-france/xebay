package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.Amount;
import fr.xebia.xebay.domain.internal.BidOfferToSell;
import fr.xebia.xebay.domain.internal.Item;
import org.junit.Test;

import static fr.xebia.xebay.domain.internal.Item.BANK;
import static org.assertj.core.api.Assertions.assertThat;

public class BankBuyEverythingTest {
    @Test
    public void item_should_be_bought_by_bank_if_selling_price_is_same_as_item_value() {
        Item item = new Item("category", "name", new Amount(4.3));
        BankBuyEverything plugin = new BankBuyEverything();

        boolean authorizeThisBidOfferToSell = plugin.authorizeIfActivated(new BidOfferToSell(item, new Amount(4.3)));

        assertThat(authorizeThisBidOfferToSell).as("item is not bought by bank so this bid offer will be sell").isFalse();
        assertThat(item.getOwner()).isEqualTo(BANK);
    }

    @Test
    public void item_should_be_not_be_bought_by_bank_if_selling_price_is_different_than_item_value() {
        Item item = new Item("category", "name", new Amount(4.3));
        BankBuyEverything plugin = new BankBuyEverything();

        boolean authorizeThisBidOfferToSell = plugin.authorizeIfActivated(new BidOfferToSell(item, new Amount(5)));

        assertThat(authorizeThisBidOfferToSell).as("item is not bought by bank so this bid offer will be sell").isTrue();
    }
}
