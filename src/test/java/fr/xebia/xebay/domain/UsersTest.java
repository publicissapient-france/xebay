package fr.xebia.xebay.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UsersTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_not_create_user_with_empty_email() {
        thrown.expect(BidException.class);
        thrown.expectMessage("can't create user without email");
        Users users = new Users();

        users.create("");
    }

    @Test
    public void should_not_create_user_with_null_email() {
        thrown.expect(BidException.class);
        thrown.expectMessage("can't create user without email");
        Users users = new Users();

        users.create(null);
    }
}
