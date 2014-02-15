package fr.xebia.xebay.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
}
