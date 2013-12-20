package bid;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    @Test
    public void when_started_user_have_1000_bidpoints() {
        BidServer bidServer = new BidServer(new Items(new Item("an item", 4.3)));
        String key = bidServer.register("email@provider.com");

        User user = bidServer.user(key);

        assertThat(user.getEmail()).isEqualTo("email@provider.com");
        assertThat(user.getKey()).isEqualTo(key);
        assertThat(user.getBalance()).isEqualTo(1000);
    }
}
