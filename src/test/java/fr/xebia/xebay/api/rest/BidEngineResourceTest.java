package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.ItemOffer;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.Items;
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
    @Mock private Items items;
    @Mock SecurityContext securityContext ;

    private User user;
    private BidEngineResource bidEngineResource;

    @Before
    public void init() {
        bidEngineResource = new BidEngineResource(bidEngine, items);
        user = new User("key1", "user1");
    }

    @Test
    public void should_return_404_if_no_bid_offer_can_be_retrieved() {
        when(bidEngine.currentBidOffer()).thenReturn(null);
        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        bidEngineResource.currentBidOffer();
    }

    @Test
    public void should_call_bidEngine_offer_when_user_offering_item() {
        Item item = new Item("category", "an item", 1.2);

        when(securityContext.getUserPrincipal()).thenReturn(user);
        when(items.find("an item")).thenReturn(item);

        bidEngineResource.offer(new ItemOffer("category", "an item", 10.0), securityContext);

        verify(bidEngine, times(1)).offer(item, 10.0, user);
    }

    @Test
    public void should_throw_404_notFoundException_when_offering_unknown_item() throws Exception {
        when(securityContext.getUserPrincipal()).thenReturn(user);

        String unknownItem = "unknown item";
        when(items.find(unknownItem)).thenReturn(null);


        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        bidEngineResource.offer(new ItemOffer("category", unknownItem, 10.0), securityContext);
    }

}
