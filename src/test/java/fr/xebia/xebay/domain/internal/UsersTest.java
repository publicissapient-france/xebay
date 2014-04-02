package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.BidException;
import fr.xebia.xebay.domain.PublicUser;
import fr.xebia.xebay.domain.UserNotAllowedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_not_create_user_with_empty_name() {
        thrown.expect(BidException.class);
        thrown.expectMessage("can't create user without name");
        Users users = new Users();

        users.create("");
    }

    @Test
    public void should_not_create_user_with_null_name() {
        thrown.expect(BidException.class);
        thrown.expectMessage("can't create user without name");
        Users users = new Users();

        users.create(null);
    }

    @Test
    public void should_not_create_already_existing_user() {
        Users users = new Users();
        users.create("user1");
        thrown.expect(BidException.class);
        thrown.expectMessage("\"user1\" is already registered");

        users.create("user1");
    }

    @Test
    public void should_not_remove_non_existing_user() {
        Users users = new Users();
        thrown.expect(UserNotAllowedException.class);
        thrown.expectMessage("key \"key\" is unknown");

        users.remove("key");
    }

    @Test
    public void should_remove_user() {
        Users users = new Users();
        String key = users.create("user1").getKey();

        User removed = users.remove(key);

        assertThat(removed.getKey()).isEqualTo(key);
        assertThat(removed.getName()).isEqualTo("user1");
        thrown.expect(UserNotAllowedException.class);
        assertThat(users.getUser(key));
    }

    @Test
    public void should_have_admin_user_by_default() {
        Users users = new Users();

        User admin = users.getUser(AdminUser.KEY);

        assertThat(admin).isNotNull().isInstanceOf(AdminUser.class);
    }

    @Test
    public void should_never_remove_admin_user() {
        Users users = new Users();
        thrown.expect(BidException.class);
        thrown.expectMessage("admin can't be removed");

        users.remove(AdminUser.KEY);
    }

    @Test
    public void should_sort_by_balance_plus_asset() {
        Users users = new Users();
        User user = users.create("user1");
        Item item = new Item("category", "item", new BigDecimal(50));
        user.buy(item);
        item.depreciate();
        users.create("user2");

        Set<PublicUser> publicUsers = users.getUsers();

        assertThat(publicUsers).containsExactly(
                new PublicUser("user2", 1000, 0),
                new PublicUser("user1", 950, 45)
        );
    }

    @Test
    public void should_return_empty_std_user_set() {
        Users users = new Users();

        Set<fr.xebia.xebay.domain.User> userSet = users.getAdminUserSet();

        assertThat(userSet).isNotNull().isEmpty();
    }

    @Test
    public void should_return_populated_user_set() {
        Users users = new Users();
        users.create("user1");
        users.create("user2");

        Set<fr.xebia.xebay.domain.User> userSet = users.getAdminUserSet();

        assertThat(userSet).isNotNull().extracting("name", String.class).containsOnly("user1", "user2");
    }


    @Test
    public void should_persist_created_users_and_not_admin() {
        Users users = new Users();
        User user1 = users.create("user1");
        user1.buy(new Item("category", "an item", new BigDecimal(4.3)));
        users.create("user2");

        Set<User> createdUsers = Users.loadUsers();
        assertThat(createdUsers).hasSize((2)).extracting("name", String.class).containsOnly("user1", "user2");
    }

    private Set<User> load_users() {
        return Users.loadUsers();
    }


}
