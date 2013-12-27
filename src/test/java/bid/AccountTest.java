package bid;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    @Test
    public void when_started_user_have_1000_bidpoints() {
        BidEngine bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
        String key = bidEngine.register("email@provider.com");

        User user = bidEngine.user(key);

        assertThat(user.getEmail()).isEqualTo("email@provider.com");
        assertThat(user.getKey()).isEqualTo(key);
        assertThat(user.getBalance()).isEqualTo(1000);
    }
}
