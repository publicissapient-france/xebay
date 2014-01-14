package bid;

import bid.api.rest.BidEngineResource;
import bid.api.rest.UserResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationTest {
    @Rule
    public ExpectedException excpectedException = ExpectedException.none();

    private Items items;

    @Before
    public void createBidOffer() {
        items = new Items(new Item("an item", 4.3));
    }

    @Test
    public void a_key_is_emmitted_when_register() {
        UserResource userResource = new UserResource(new Users());

        String key = userResource.register("an-email@provider.com");

        assertThat(key).matches(Pattern.compile("[\\w\\-_]+")).hasSize(16);
    }

    @Test
    public void can_not_register_twice() {
        UserResource userResource = new UserResource(new Users());
        userResource.register("an-email@provider.com");
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("\"an-email@provider.com\" is already registered");

        userResource.register("an-email@provider.com");
    }

    //todo move
    @Test
    public void once_registered_key_allow_user_to_interact_with_server() {
        Users users = new Users();
        UserResource userResource = new UserResource(users);
        String key = userResource.register("an-email@provider.com");

        BidEngineResource bidEngineResource = new BidEngineResource(users);
        BidOffer bidOffer = bidEngineResource.currentBidOffer();

        //todo bidEngineResource.bid(key, bidOffer.getItem().getName(),);
    }

    @Test
    public void cant_get_user_if_key_is_not_correct() {
        UserResource userResource = new UserResource(new Users());
        excpectedException.expect(UserNotAllowedException.class);
        excpectedException.expectMessage("key \"fake key\" is unknown");

        userResource.getUser("fake key");
    }

    //todo move
    @Test
    public void cant_bid_if_no_user() {
        BidEngine bidEngine = new BidEngine(items);
        excpectedException.expect(UserNotAllowedException.class);
        excpectedException.expectMessage("bad user");

        bidEngine.bid(null, "an item", 4.3, 5);//todo fake user without right
    }
}
