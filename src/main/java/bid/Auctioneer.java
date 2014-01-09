package bid;

import com.google.gson.Gson;

import static java.util.Collections.synchronizedList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/auctioneer")
@ServerEndpoint(value = "/ws/auctioneer")
public class Auctioneer {

    static final Gson gson = new Gson();

    // Test d'un Client WebSocket en Java
    // WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    // container.connectToServer(Client.class, new URI("ws://localhost:8080/ws/auctioneer"));

    @GET
    @Path("/register")
    public String register() {
        // Générer une clé aléatoire unique
        return UUID.randomUUID().toString();
    }

    @GET
    @Path("/item")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItem() {
        return new Item("Test", 10);
    }

    @OnMessage
    public void onMessage(Session session, String message) {

        BidOffer bidOffer = gson.fromJson(message, BidOffer.class);

        // TODO vérifier la validité et le placement de l'offre

        session.getOpenSessions().forEach(openedSession -> {

            // TODO renvoyer le message à l'expéditeur également ?
            if (openedSession.isOpen()) {
                try {
                    openedSession.getBasicRemote().sendText("message received");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
