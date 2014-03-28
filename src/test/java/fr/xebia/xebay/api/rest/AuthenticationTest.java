package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.dto.BidDemand;
import fr.xebia.xebay.api.rest.security.SecurityContextImpl;
import fr.xebia.xebay.domain.BidException;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import fr.xebia.xebay.domain.BidOffer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationTest {
    @Rule
    public ExpectedException excpectedException = ExpectedException.none();

    @Test
    public void a_key_is_emmitted_when_register() {
        UserResource userResource = new UserResource(new Users());
        String key = userResource.register("user1");

        assertThat(key).matches(Pattern.compile("[\\w\\-_]+")).hasSize(16);
    }

    @Test
    public void can_not_register_twice() {
        UserResource userResource = new UserResource(new Users());
        userResource.register("user1");
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("\"user1\" is already registered");

        userResource.register("user1");
    }

    @Test
    public void cant_bid_if_no_user() {
        excpectedException.expect(BidException.class);
        excpectedException.expectMessage("bad user");

        BidEngineResource bidEngineResource = new BidEngineResource();

        User badUser = mock(User.class);
        when(badUser.getName()).thenReturn(null);

        bidEngineResource.bid(new BidDemand("an item", 4.3 + 20.0), new SecurityContextImpl(badUser)); //todo fake user without right
    }

    @Test
    public void a_user_can_interact_with_server() {
        BidEngineResource bidEngineResource = new BidEngineResource();
        BidOffer bidOffer = bidEngineResource.currentBidOffer();

        bidEngineResource.bid(new BidDemand(bidOffer.getItem().getName(), bidOffer.getItem().getValue() + 20.0), new SecurityContextImpl(new User("abc", "user1")));
    }
}
