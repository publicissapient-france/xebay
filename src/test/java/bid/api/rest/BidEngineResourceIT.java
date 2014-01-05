package bid.api.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class BidEngineResourceIT {
    //private HttpServer server;
    private WebTarget target;

    @Rule
    public ExpectedException excpectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/rest/bidEngine/");
    }

    @After
    public void tearDown() throws Exception {
        target = null;
    }

    @Test
    public void testCurrentBidOffer() throws Exception {

    }

    @Test
    public void register_should_create_new_user() throws Exception {
        String key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());
    }

    @Test
    public void register_should_throw_exception_if_already_registered_user() throws Exception {
        String key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());

/*todo
        key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        excpectedException.expect(ForbiddenException.class);
        excpectedException.expectMessage("\"an-email@provider.com\" is already registered");
*/
    }

    @Test
    public void testBid() throws Exception {

    }
}
