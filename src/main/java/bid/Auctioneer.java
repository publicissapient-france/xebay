package bid;

import bid.domain.BidOffer;
import bid.domain.Item;
import com.google.gson.Gson;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.UUID;

@Path("/auctioneer")
@ServerEndpoint(value = "/ws/auctioneer")
public class Auctioneer {

    static final Gson gson = new Gson();


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
        bidOffer.setTimeToLive(10 * 1000);

        // TODO vérifier la validité et le placement de l'offre

        session.getOpenSessions().forEach(openedSession -> {

            // TODO renvoyer le message à l'expéditeur également ?

            if (openedSession.isOpen()) {
                try {
                    openedSession.getBasicRemote().sendText(gson.toJson(bidOffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}