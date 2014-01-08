package bid.api.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class BidEngineResourceIT {
    private WebTarget target;
    private String key ;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/rest/bidEngine/");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCurrentBidOffer() throws Exception {
    }

    @Test
    public void testBid() throws Exception {

    }
}
