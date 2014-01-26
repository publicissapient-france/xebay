package fr.xebia.xebay.api.socket;

import com.google.gson.Gson;
import fr.xebia.xebay.api.rest.dto.BidOfferInfo;
import fr.xebia.xebay.api.socket.dto.BidAnswer;
import fr.xebia.xebay.api.socket.dto.BidAnswerType;
import fr.xebia.xebay.api.socket.dto.BidCall;
import fr.xebia.xebay.utils.TomcatRule;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.*;

import javax.websocket.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
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
  static WebSocketContainer container = ContainerProvider.getWebSocketContainer();
  static WebTarget target = null;
  static String key = null;

  List<BidAnswer> bidAnswerList = new ArrayList<>();

  @BeforeClass
  public static void before() throws URISyntaxException, IOException, DeploymentException {
    Client client = ClientBuilder.newClient().register(JacksonFeature.class);
    target = client.target("http://localhost:8080/rest/");
    key = target.path("users/register").queryParam("email", "test@test.fr").request().get(String.class);
  }

  @Test(timeout = 5000)
  public void test_bad_key_is_not_allowed() throws Exception {

    BidCall bidCall = new BidCall("an item", 4.3, 10);

    sendCallAndWait("ws://localhost:8080/socket/bidEngine/badkey", bidCall, 1);

    BidAnswer bidAnswer = bidAnswerList.get(0);
    Assertions.assertThat(bidAnswer.getType()).isEqualTo(BidAnswerType.REJECTED);
    Assertions.assertThat(bidAnswer.getName()).isEqualTo("an item");
    Assertions.assertThat(bidAnswer.getValue()).isEqualTo(4.3, Offset.offset(0d));
    Assertions.assertThat(bidAnswer.getIncrement()).isEqualTo(10, Offset.offset(0d));
  }

  @Test(timeout = 5000)
  public void test_bad_call_is_not_allowed() throws Exception {

    BidCall bidCall = new BidCall("not a valid item", 4.5, 10);

    sendCallAndWait("ws://localhost:8080/socket/bidEngine/" + key, bidCall, 1);

    BidAnswer bidAnswer = bidAnswerList.get(0);
    Assertions.assertThat(bidAnswer.getType()).isEqualTo(BidAnswerType.REJECTED);
    Assertions.assertThat(bidAnswer.getName()).isEqualTo("not a valid item");
    Assertions.assertThat(bidAnswer.getValue()).isEqualTo(4.5, Offset.offset(0d));
    Assertions.assertThat(bidAnswer.getIncrement()).isEqualTo(10, Offset.offset(0d));
  }

  @Test(timeout = 5000)
  public void test_good_call_is_allowed_and_then_notified() throws Exception {

    BidOfferInfo currentBidOffer = target.path("bidEngine/current").request().get(BidOfferInfo.class);
    BidCall bidCall = new BidCall(currentBidOffer.getItemName(), currentBidOffer.getCurrentValue(), 10);

    sendCallAndWait("ws://localhost:8080/socket/bidEngine/" + key, bidCall, 1);

    BidAnswer infoAnswer = bidAnswerList.get(0);
    Assertions.assertThat(infoAnswer.getType()).isEqualTo(BidAnswerType.ACCEPTED);
    Assertions.assertThat(infoAnswer.getName()).isEqualTo("an item");
    Assertions.assertThat(infoAnswer.getValue()).isEqualTo(currentBidOffer.getCurrentValue() + 10, Offset.offset(0d));

  }

  @OnMessage
  public void onMessage(String message) {
    bidAnswerList.add(gson.fromJson(message, BidAnswer.class));
  }

  public void sendCallAndWait(String url, BidCall bidCall, int count) throws Exception {

    bidAnswerList.clear();

    URI uri = new URI(url);
    Session session = container.connectToServer(this, uri);
    session.getBasicRemote().sendText(gson.toJson(bidCall));

    while (bidAnswerList.size() < count) {
      Thread.sleep(500);
    }
  }

  @AfterClass
  public static void after() throws IOException {
    target.path("unregister").request().header(HttpHeaders.AUTHORIZATION, key).delete();
  }
}

