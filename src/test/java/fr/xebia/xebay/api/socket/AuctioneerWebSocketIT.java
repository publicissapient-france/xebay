package fr.xebia.xebay.api.socket;

import com.google.gson.Gson;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.utils.TomcatRule;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Ignore // FIXME désactivé le temps de faire le branchement sur bidengine
@ClientEndpoint
public class AuctioneerWebSocketIT {

    @ClassRule
    public static TomcatRule tomcatRule = new TomcatRule();

    static Gson gson = new Gson();

    Session session = null;

    BidOffer offerReceived = null;

    @Before
    public void before() throws URISyntaxException, IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://localhost:8080/socket/auctioneer");
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

        Assertions.assertThat(offerReceived.getItem()).isNotNull();
        Assertions.assertThat(offerReceived.getItem().getName()).isEqualTo("Test");
        Assertions.assertThat(offerReceived.getItem().getValue()).isEqualTo(2d, Offset.offset(0d));
    }

    @After
    public void after() throws IOException {
        session.close();
        session = null;
        offerReceived = null;
    }

}

