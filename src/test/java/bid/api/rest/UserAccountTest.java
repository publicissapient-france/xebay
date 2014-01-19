package bid.api.rest;

import bid.domain.User;
import bid.domain.Users;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTest {
    @Test
    public void when_started_user_have_1000_bidpoints() {
        UserResource userResource = new UserResource(new Users());
        String key = userResource.register("email@provider.com");

        User user = userResource.getUser(key);

        assertThat(user.getEmail()).isEqualTo("email@provider.com");
        assertThat(user.getKey()).isEqualTo(key);
        assertThat(user.getBalance()).isEqualTo(1000);
    }
}
