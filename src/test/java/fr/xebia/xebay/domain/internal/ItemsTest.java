package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.BidException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsTest {
    @Test
    public void load_one_item() {
        Items items = Items.load("item").get();

        Item item = items.next();
        assertThat(item.getCategory()).isEqualTo("category");
        assertThat(item.getName()).isEqualTo("item");
        assertThat(item.getValue()).isEqualTo(new BigDecimal(1));
    }

    @Test
    public void load_two_items() {
        Items items = Items.load("items-two").get();
        items.next();

        Item secondItem = items.next();
        assertThat(secondItem.getName()).isEqualTo("second item");
        assertThat(secondItem.getValue()).isEqualTo(new BigDecimal(2));
    }

    @Test
    public void load_item_with_accented_chars() {
        Items items = Items.load("items-accented-chars").get();

        Item item = items.next();
        assertThat(item.getName()).isEqualTo("itèm with accented character");
    }

    @Test
    public void load_item_with_comma() {
        Items items = Items.load("items-comma").get();

        Item item = items.next();
        assertThat(item.getName()).isEqualTo("item with a , comma");
    }

    @Test
    public void load_item_with_trimmed_columns() {
        Items items = Items.load("items-trim").get();

        Item item = items.next();
        assertThat(item.getName()).isEqualTo("item");
        assertThat(item.getValue()).isEqualTo(new BigDecimal(1));
    }

    @Test(expected = BidException.class)
    public void can_t_create_items_without_any_item() {
        new Items();
    }

    @Test
    public void should_returns_first_item() {
        Items items = new Items(new Item("category", "an item", new BigDecimal(4.3)));

        Item item = items.next();

        assertThat(item.getName()).isEqualTo("an item");
    }

    @Test
    public void should_returns_second_item() {
        Items items = new Items(new Item("category", "an item", new BigDecimal(4.3)), new Item("category", "another item", new BigDecimal(2.4)));
        items.next();

        Item item = items.next();

        assertThat(item.getName()).isEqualTo("another item");
    }

    @Test
    public void should_loop_througth_items() {
        Items items = new Items(new Item("category", "an item", new BigDecimal(4.3)), new Item("category", "another item", new BigDecimal(2.4)));
        items.next();
        items.next();

        Item item = items.next();

        assertThat(item.getName()).isEqualTo("an item");
    }

    @Test
    public void should_not_get_item_if_owned_by_a_user() {
        Item item = new Item("category", "an item", new BigDecimal(4.3));
        item.concludeTransaction(new BigDecimal(5), new User("", "user1"));
        Items items = new Items(item);

        Item nextItem = items.next();

        assertThat(nextItem).isNull();
    }

    @Test
    public void should_not_get_item_if_there_are_all_owned_by_a_user() {
        User buyer = new User("", "user1");
        Item firstItem = new Item("category", "an item", new BigDecimal(4.3));
        firstItem.concludeTransaction(new BigDecimal(5), buyer);
        Item secondItem = new Item("category", "another item", new BigDecimal(5));
        secondItem.concludeTransaction(new BigDecimal(2.4), buyer);
        Items items = new Items(firstItem, secondItem);

        Item nextItem = items.next();

        assertThat(nextItem).isNull();
    }

    @Test
    public void should_get_an_item_by_his_name() {
        Item item = new Item("category", "an item", new BigDecimal(4.3));
        Items items = new Items(item);

        Item itemByName = items.get("an item");

        assertThat(itemByName).isEqualTo(item);
    }
}
