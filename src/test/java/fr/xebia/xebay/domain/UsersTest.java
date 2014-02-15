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
        thrown.expect(BidException.class);
        thrown.expectMessage("\"user1\" is not registered");

        users.remove(null, "user1");
    }

    @Test
    public void should_not_remove_non_existing_name() {
        Users users = new Users();
        users.create("user1");
        String key = users.create("user2").getKey();
        thrown.expect(BidException.class);
        thrown.expectMessage("\"user1\" is registered but bad name");

        users.remove(key, "user1");
    }

    @Test
    public void should_remove_user() {
        Users users = new Users();
        String key = users.create("user1").getKey();

        users.remove(key, "user1");

        thrown.expect(UserNotAllowedException.class);
        assertThat(users.getUser(key));
    }

    @Test
    public void should_have_admin_user_by_default() {
        Users users = new Users();

        User admin = users.getUser(AdminUser.KEY);

        assertThat(admin).isNotNull().isInstanceOf(AdminUser.class);
    }
}
