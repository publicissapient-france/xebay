package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.RegisterRule;
import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.utils.TomcatRule;
import org.assertj.core.data.Offset;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.*;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ClientEndpoint
public class BidEngineSocketIT {
    @ClassRule
    public static TomcatRule tomcatRule = new TomcatRule();

    private static ObjectMapper gson = new ObjectMapper();

    @Rule
    public RegisterRule registerRule = new RegisterRule();

    private List<BidOffer> bidOfferList = new ArrayList<>();
    private WebTarget target = null;

    @Before
    public void before() throws URISyntaxException, IOException, DeploymentException {
        target = ClientBuilder.newClient().register(JacksonFeature.class).target("http://localhost:8080/rest");
        ContainerProvider.getWebSocketContainer().connectToServer(this, new URI("ws://localhost:8080/socket/bidEngine"));
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            BidOffer bidOffer = gson.readValue(message, BidOffer.class);
            bidOfferList.add(bidOffer);
        } catch (IOException e) {
            fail(format("error during reading %s", message), e);
        }
        synchronized (this) {
            this.notify();
        }
    }

    @After
    public void after() throws IOException {
        bidOfferList.clear();
    }

    @Test(timeout = 5000)
    public void test_good_demand_is_notified() throws Exception {
        BidOffer currentBidOffer = target.path("bidEngine/current").request().get(BidOffer.class);
        BidDemand bidDemand = new BidDemand(currentBidOffer.getItemName(), currentBidOffer.getCurrentValue(), 10);

        target.path("bidEngine/bid").request().header(HttpHeaders.AUTHORIZATION, registerRule.getKey())
                .post(Entity.entity(bidDemand, MediaType.APPLICATION_JSON));
        synchronized (this) {
            this.wait();
        }

        BidOffer bidOffer = bidOfferList.get(0);
        assertThat(bidOffer.getItemName()).isEqualTo("an item");
        assertThat(bidOffer.getCurrentValue()).isEqualTo(currentBidOffer.getCurrentValue() + 10, Offset.offset(0d));
    }
}

