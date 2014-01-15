package bid.api.rest;

import bid.BidOffer;
import bid.test.TomcatRule;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class BidEngineResourceIT {
    private static final Logger log = Logger.getLogger("BidEngineResourceIT");

    private Client client;
    private WebTarget target;
    private String key ;

    @ClassRule
    public static TomcatRule tomcatRule = new TomcatRule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        target = client.target("http://localhost:8080/rest/bidEngine");
    }

    @After
    public void tearDown() throws Exception {
        target = null;
        client.close();
    }

    private void register() throws Exception {
        key = client.target("http://localhost:8080/rest/users/register").queryParam("email", "aaa@eee.com").request().get(String.class);
    }

    private void unregister() throws Exception {
       key = client.target("http://localhost:8080/rest/users/unregister").queryParam("email","aaa@eee.com").queryParam("key",key).request().get(String.class);
    }


    @Test
    public void current_bidOffer_can_be_retrieved() throws Exception {
        BidOffer bidOffer = target.request().get(BidOffer.class);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(4.3);
        assertThat(bidOffer.getTimeToLive()).isEqualTo(0);
    }


    @Test
    public void a_registered_user_can_post_form_bid() throws Exception {
        register();

        BidOffer currentBidOffer = target.request().get(BidOffer.class);

        Form form = new Form();
        form.param("name", currentBidOffer.getItem().getName());
        form.param("value", String.valueOf(currentBidOffer.getCurrentValue()));
        form.param("increment", "0.7");

        BidOffer bidOffer = target.path("/bid").request()
                .header(HttpHeaders.AUTHORIZATION, key)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), BidOffer.class);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(5);
        assertThat(bidOffer.getTimeToLive()).isEqualTo(0);

		unregister();
    }

    @Test
    public void cant_bid_if_user_not_registered() {
        String fakeKey = "fake key";

        BidOffer currentBidOffer = target.request().get(BidOffer.class);
        Form form = new Form();
        form.param("name", currentBidOffer.getItem().getName());
        form.param("value", String.valueOf(currentBidOffer.getCurrentValue()));
        form.param("increment", "0.7");

        expectedException.expect(NotAuthorizedException.class);
        expectedException.expectMessage("HTTP 401 Unauthorized");

        target.path("/bid").request()
                .header(HttpHeaders.AUTHORIZATION, fakeKey)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), BidOffer.class);
    }

    @Test
    public void a_registered_user_can_bid() throws Exception {
        register();
        log.info("key"+ key);

        BidOffer currentBidOffer = target.request().get(BidOffer.class);

        BidParam bidParam = new BidParam(currentBidOffer.getItem().getName(), currentBidOffer.getCurrentValue(), 0.7);

        BidOffer bidOffer = target.path("/bid").request()
                .header(HttpHeaders.AUTHORIZATION, key)
                .post(Entity.entity(bidParam, MediaType.APPLICATION_JSON_TYPE), BidOffer.class);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(5);
        assertThat(bidOffer.getTimeToLive()).isEqualTo(0);

        unregister();

    }

}
