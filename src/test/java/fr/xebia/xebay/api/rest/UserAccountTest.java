package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.domain.User;
import fr.xebia.xebay.domain.Users;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTest {
    @Test
    public void when_started_user_have_1000_bidpoints() {
        UserResource userResource = new UserResource(new Users());
        String key = userResource.register("user1");

        User user = userResource.getUser(key);

        assertThat(user.getName()).isEqualTo("user1");
        assertThat(user.getKey()).isEqualTo(key);
        assertThat(user.getBalance()).isEqualTo(1000);
    }
}
