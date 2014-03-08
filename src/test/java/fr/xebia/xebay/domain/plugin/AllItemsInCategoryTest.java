package fr.xebia.xebay.domain.plugin;

import fr.xebia.xebay.domain.internal.BidOffer;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import org.junit.Before;
import org.junit.Test;

import static fr.xebia.xebay.domain.BidEngine.DEFAULT_TIME_TO_LIVE;
import static fr.xebia.xebay.domain.internal.Item.BANK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllItemsInCategoryTest {
    private User userToCredit;
    private User otherUser;

    @Before
    public void createUsers() {
        userToCredit = new User("key1", "userToCredit");
        otherUser = new User("key2", "otherUser");
    }

    @Test
    public void on_activation_should_credit_user_if_he_has_all_items_of_category() {
        Items items = new Items(
                newItem("category", userToCredit)
        );
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onActivation(items);

        assertThat(userToCredit.getBalance()).isEqualTo(1500);
    }

    @Test
    public void on_activation_should_not_credit_user_if_other_users_has_items_of_same_category() {
        Items items = new Items(
                newItem("category", userToCredit),
                newItem("category", otherUser),
                newItem("category", BANK)
        );
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onActivation(items);

        assertThat(userToCredit.getBalance()).isEqualTo(1000);
    }

    @Test
    public void on_activation_should_credit_user_for_each_category() {
        Items items = new Items(
                newItem("category1", userToCredit),
                newItem("category2", userToCredit)
        );
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onActivation(items);

        assertThat(userToCredit.getBalance()).isEqualTo(2000);
    }

    @Test
    public void on_activation_should_not_credit_bank() {
        Items items = new Items(
                newItem("category", BANK)
        );
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onActivation(items);
    }

    @Test
    public void on_bid_should_credit_user_if_he_has_all_items_of_category() {
        Item item = newItem("category", userToCredit);
        Items items = new Items(item);
        BidOffer bidOffer = new BidOffer(item, DEFAULT_TIME_TO_LIVE);
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onBidOfferResolvedIfActivated(bidOffer, items);

        assertThat(userToCredit.getBalance()).isEqualTo(1500);
    }

    @Test
    public void on_bid_should_not_credit_user_if_other_users_has_items_of_same_category() {
        Item item = newItem("category", userToCredit);
        Items items = new Items(
                item,
                newItem("category", otherUser)
        );
        BidOffer bidOffer = new BidOffer(item, DEFAULT_TIME_TO_LIVE);
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onBidOfferResolvedIfActivated(bidOffer, items);

        assertThat(userToCredit.getBalance()).isEqualTo(1000);
    }

    @Test
    public void on_bid_should_not_credit_bank() {
        Item item = newItem("category", BANK);
        Items items = new Items(item);
        BidOffer bidOffer = new BidOffer(item, DEFAULT_TIME_TO_LIVE);
        AllItemsInCategory plugin = new AllItemsInCategory();

        plugin.onBidOfferResolvedIfActivated(bidOffer, items);
    }

    private Item newItem(String category, User owner) {
        Item item = mock(Item.class);
        when(item.getCategory()).thenReturn(category);
        when(item.getOwner()).thenReturn(owner);
        return item;
    }
}
