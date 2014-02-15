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
    public void should_have_admin_user_by_default() {
        Users users = new Users();

        User admin = users.getUser(AdminUser.KEY);

        assertThat(admin).isNotNull().isInstanceOf(AdminUser.class);
    }
}
