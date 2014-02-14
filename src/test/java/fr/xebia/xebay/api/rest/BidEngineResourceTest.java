package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.ItemOffer;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BidEngineResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock private BidEngine bidEngine;
    private User user;
    private BidEngineResource bidEngineResource;

    @Before
    public void init() {
        bidEngineResource = new BidEngineResource(bidEngine);
        user = new User("key1", "user1");
    }

    @Test
    public void should_return_404_if_no_bid_offer_can_be_retrieved() {
        when(bidEngine.currentBidOffer()).thenReturn(null);
        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        new BidEngineResource(bidEngine).currentBidOffer();
    }

    @Test
    public void should_call_bidEngine_offer_when_user_offering_item() {
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getUserPrincipal()).thenReturn(user);

        ItemOffer itemOffer = new ItemOffer("an item", 10.0);

        bidEngineResource.offer(itemOffer, securityContext);

        verify(bidEngine, times(1)).offer(user, "an item", 10.0);
    }
    //TODO test should check NotFoundException @Test(expected = BidException.class)
    public void should_throw_notFoundException_when_offering_unknown_item() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getUserPrincipal()).thenReturn(user);

        String itemName = "unknown item";

//        doThrow(new BidException("unknown item not found")).when(bidEngine).offer(user, itemName, 10.0);

//        bidEngineResource.offer(new ItemOffer(itemName, 10.0), securityContext);
    }

}
