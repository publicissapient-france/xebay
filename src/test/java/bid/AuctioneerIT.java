package bid;

import bid.test.TomcatRule;
import com.google.gson.Gson;
import org.junit.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class AuctioneerIT {

  @ClassRule
  public static TomcatRule tomcatRule = new TomcatRule();

  static Gson gson = new Gson();

  Session session = null;

  BidOffer offerReceived = null;

  @Before
  public void before() throws URISyntaxException, IOException, DeploymentException {
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    URI uri = new URI("ws://localhost:8080/ws/auctioneer");
    session = container.connectToServer(this, uri);
  }

  @OnMessage
  public void onMessage(String message) {
    offerReceived = gson.fromJson(message, BidOffer.class);
  }

  @Test(timeout = 5000)
  public void test_bidoffer_is_notified() throws InterruptedException, IOException {
    Item itemSent = new Item("Test", 2);
    BidOffer offerSent = new BidOffer(itemSent);
    String message = gson.toJson(offerSent);

    session.getBasicRemote().sendText(message);
    while (offerReceived == null) {
      Thread.sleep(500);
    }

    Assert.assertNotNull(offerReceived.getItem());
    Assert.assertEquals("Test", offerReceived.getItem().getName());
    Assert.assertEquals(2, offerReceived.getItem().getValue(), 0);
  }

  @After
  public void after() throws IOException {
    session.close();
    session = null;
    offerReceived = null;
  }

}

