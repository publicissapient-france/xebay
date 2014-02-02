package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.domain.BidEngine;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BidEngineResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private BidEngine bidEngine;

    @Test
    public void should_return_404_if_no_bid_offer_can_be_retrieved() {
        when(bidEngine.currentBidOffer()).thenReturn(null);
        thrown.expect(WebApplicationException.class);
        thrown.expectMessage("HTTP 404 Not Found");

        new BidEngineResource(bidEngine).currentBidOffer();
    }
}
