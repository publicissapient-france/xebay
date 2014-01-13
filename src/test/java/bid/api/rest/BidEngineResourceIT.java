package bid.api.rest;

import bid.BidOffer;
import bid.test.TomcatRule;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class BidEngineResourceIT {
    private Client client;
    private WebTarget target;
    private String key ;

    @ClassRule
    public static TomcatRule tomcatRule = new TomcatRule();



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
    public void testCurrentBidOffer() throws Exception {
        BidOffer bidOffer = target.request().get(BidOffer.class);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(4.3);
        assertThat(bidOffer.getTimeToLive()).isEqualTo(0);
    }


    @Test
    public void testBid() throws Exception {
        register();

        BidOffer currentBidOffer = target.request().get(BidOffer.class);

        Form form = new Form();
        form.param("name", currentBidOffer.getItem().getName());
        form.param("value", String.valueOf(currentBidOffer.getCurrentValue()));
        form.param("increment", "0.7");
        form.param("key", key);

        BidOffer bidOffer = target.path("/bid").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), BidOffer.class);
        assertThat(bidOffer.getItem().getName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(5);
        assertThat(bidOffer.getTimeToLive()).isEqualTo(0);

		unregister();

    }
}
