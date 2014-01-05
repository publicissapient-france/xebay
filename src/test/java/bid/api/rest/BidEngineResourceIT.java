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
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BidEngineResourceIT {

    private Client client;
    private WebTarget target;
    private String key ;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/rs/bidEngine/");
    }

    @After
    public void tearDown() throws Exception {
        if(null != key){
            target.path("unregister")
                    .path("abc@def.ghi")
                    .queryParam("key", key).request().get();
        }
        target = null;
        client.close();
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
            fail();
        } catch (ForbiddenException e) {
            assertEquals("\"abc@def.ghi\" is already registered", e.getResponse().readEntity(String.class));
        }
    }

    @Test
    public void testBid() throws Exception {

    }
}
