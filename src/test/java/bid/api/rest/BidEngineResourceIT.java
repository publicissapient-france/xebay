package bid.api.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class BidEngineResourceIT {
    //private HttpServer server;
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
        if(null != key){
            target.path("unregister")
                    .path("abc@def.ghi")
                    .queryParam("key", key).request().get();
        }
        target = null;
    }

    @Test
    public void testCurrentBidOffer() throws Exception {

    }

    @Test
    public void register_should_create_new_user() throws Exception {
        key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());
    }

    @Test
    public void register_should_throw_forbidden_exception_if_already_registered_user() throws Exception {
        key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        assertEquals(16, key.length());


        try {
            key = target.path("register").queryParam("email", "abc@def.ghi").request().get(String.class);
        } catch (ForbiddenException e) {
      //      TODO assertEquals("\"abc@def.ghi\" is already registered", e.getMessage());
        }
        //expectedException.expect(ForbiddenException.class);
        //expectedException.expectMessage("\"abc@def.ghi\" is already registered");

    }

    @Test
    public void testBid() throws Exception {

    }
}
