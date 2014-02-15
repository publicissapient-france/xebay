package fr.xebia.xebay.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void should_create_a_user() {
        User user = new User("key", "user1");

        assertThat(user.getKey()).isEqualTo("key");
        assertThat(user.getName()).isEqualTo("user1");
        assertThat(user.getItems()).isEmpty();
        assertThat(user.getBalance()).isEqualTo(1000);
        assertThat(user.toString()).isEqualTo("user1");
    }

    @Test
    public void two_users_are_equals_if_they_have_same_name() {
        User user1 = new User("key1", "user1");
        User user1WithAnotherKey = new User("key2", "user1");

        assertThat(user1).isEqualTo(user1WithAnotherKey);
        assertThat(user1.hashCode()).isEqualTo(user1WithAnotherKey.hashCode());
    }

    @Test
    public void a_user_has_no_role() {
        User user = new User("key", "user");

        boolean isUserInSomeRole = user.isInRole("some role");

        assertThat(isUserInSomeRole).isFalse();
    }

    @Test
    public void a_user_can_buy_an_item() {
        Item item = new Item("item", 20);
        User user = new User("key", "user");

        user.buy(item);

        assertThat(user.getBalance()).isEqualTo(980);
        assertThat(user.getItems()).containsExactly(item);
    }

    @Test
    public void a_user_can_sell_an_item() {
        Item item = new Item("item", 20);
        User user = new User("key", "user");
        user.buy(item);

        user.sell(item);

        assertThat(user.getBalance()).isEqualTo(1000);
        assertThat(user.getItems()).isEmpty();
    }

    @Test
    public void a_user_can_bid_item_lower_than_his_balance() {
        User user = new User("key", "user");

        boolean canBid = user.canBid(20);

        assertThat(canBid).isTrue();
    }

    @Test
    public void a_user_can_t_bid_item_higher_than_his_balance() {
        User user = new User("key", "user");

        boolean canBid = user.canBid(1001);

        assertThat(canBid).isFalse();
    }

    @Test
    public void a_user_never_implies_any_subject() {
        User user = new User("key", "user");

        boolean impliesSubject = user.implies(null);

        assertThat(impliesSubject).isFalse();
    }
}
