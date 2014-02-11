package fr.xebia.xebay.api.socket;

import com.google.gson.Gson;
import fr.xebia.xebay.domain.BidDemand;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.utils.TomcatRule;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

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

@ClientEndpoint
public class BidEngineSocketIT {

  @ClassRule
  public static TomcatRule tomcatRule = new TomcatRule();
  static Gson gson = new Gson();

  List<BidOffer> bidOfferList = new ArrayList<>();
  WebTarget target = null;
  String key = null;

  @Before
  public void before() throws URISyntaxException, IOException, DeploymentException {
    target = ClientBuilder.newClient().register(JacksonFeature.class).target("http://localhost:8080/rest");
    key = target.path("users/register").queryParam("email", "test@test.fr").request().get(String.class);
    ContainerProvider.getWebSocketContainer().connectToServer(this, new URI("ws://localhost:8080/socket/bidEngine"));
  }

  @OnMessage
  public void onMessage(String message) {
    BidOffer bidOffer = gson.fromJson(message, BidOffer.class);
    bidOfferList.add(bidOffer);
    synchronized (this) {
      this.notify();
    }
  }

  @After
  public void after() throws IOException {
    bidOfferList.clear();
    target.path("users/unregister").request().header(HttpHeaders.AUTHORIZATION, key).delete();
  }

  @Test(timeout = 5000)
  public void test_good_demand_is_notified() throws Exception {

    BidOffer currentBidOffer = target.path("bidEngine/current").request().get(BidOffer.class);
    BidDemand bidDemand = new BidDemand(currentBidOffer.getItemName(), currentBidOffer.getCurrentValue(), 10);

    target.path("bidEngine/bid").request().header(HttpHeaders.AUTHORIZATION, key)
            .post(Entity.entity(bidDemand, MediaType.APPLICATION_JSON));
    synchronized (this) {
      this.wait();
    }

    BidOffer bidOffer = bidOfferList.get(0);
    Assertions.assertThat(bidOffer.getItemName()).isEqualTo("an item");
    Assertions.assertThat(bidOffer.getCurrentValue()).isEqualTo(currentBidOffer.getCurrentValue() + 10, Offset.offset(0d));

  }
}

