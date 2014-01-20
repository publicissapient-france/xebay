package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;
import com.google.gson.Gson;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/socket/auctioneer",
        encoders = WebSocketCoder.BidOfferEncoder.class,
        decoders = WebSocketCoder.WebSocketMessageDecoder.class)
public class AuctioneerWebSocket {

  static final Gson gson = new Gson();

  Session session = null;

  @OnOpen
  public void onOpen(Session session) {
    if (this.session == null) {
      this.session = session;
    }
  }

  @OnMessage
  public void onMessage(Session session, WebSocketMessage message) {

    // FIXME Utiliser un objet englobant les messages ou alors faire un switch sur un Content-Type en début de message ?

    if (message.getBidOffer() != null) {
      // TODO notify bidEngine
    }

    if (message.getItemListToSell() != null) {
      // TODO notify bidEngine message.getItemListToSell().forEach(itemToSell -> );
    }
  }

  public void broadcastBidOffer(BidOffer bidOffer) {

    session.getOpenSessions().forEach(openedSession -> {

      if (openedSession.isOpen()) {
        try {
          openedSession.getBasicRemote().sendText(gson.toJson(bidOffer));
        } catch (IOException e) {
          e.printStackTrace(); // FIXME
        }
      }
    });
  }
}