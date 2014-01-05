package bid;

import bid.api.rest.BidEncoders.BidOfferDecoder;
import bid.api.rest.BidEncoders.BidOfferEncoder;

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

@Singleton
@Path("/auctioneer")
@ServerEndpoint(value = "/ws/auctioneer/listen",
        encoders = {BidOfferEncoder.class},
        decoders = {BidOfferDecoder.class})
public class Auctioneer {

    Integer scheduled = 0;

    final List<Session> sessionList = synchronizedList(new ArrayList<Session>());

    // Test d'un Client WebSocket en Java
    // WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    // container.connectToServer(Client.class, new URI("ws://localhost:8080/Test/WebSocket"));

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

    @OnOpen
    public void onOpen(Session session) {
        sessionList.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // TODO Que faire des messages reçus des clients ?
        System.out.println(message);
    }

    @Schedule(second = "*/5", minute = "*", hour = "*")
    public void refresh() throws IOException, EncodeException {
        ++scheduled;
        synchronized (sessionList) {
            List<Session> sessionClosedList = new ArrayList<>();
            for (Session session : sessionList) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText("News from server #" + scheduled);
                    session.getBasicRemote().sendObject(new BidOffer(new Item("Name", 10)));
                } else {
                    sessionClosedList.add(session);
                }
            }
            sessionList.removeAll(sessionClosedList);
        }
    }
}
