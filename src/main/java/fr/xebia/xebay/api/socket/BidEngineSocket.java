package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.socket.coder.BidCallDecoder;
import fr.xebia.xebay.api.socket.coder.BidOfferEncoder;
import fr.xebia.xebay.api.socket.dto.BidCall;
import fr.xebia.xebay.domain.*;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@ServerEndpoint(value = "/socket/bidEngine/{authToken}", encoders = BidOfferEncoder.class, decoders = BidCallDecoder.class)
public class BidEngineSocket implements BidEngineListener {

  Session session = null;

  @OnOpen
  public void onOpen(Session session) {
    if (this.session == null) {
      this.session = session;
    }
  }

  @OnMessage
  public void onMessage(Session session, @PathParam("authToken") String authToken, BidCall bidCall) throws IOException {

    User user;
    try {
      user = BID_SERVER.users.getUser(authToken);
    } catch (UserNotAllowedException e) {
      session.getBasicRemote().sendText(e.getMessage());
      return;
    }

    try {
      BID_SERVER.bidEngine.bid(user, bidCall.getItemName(), bidCall.getCurValue(), bidCall.getIncrement());
    } catch (BidException e) {
      session.getBasicRemote().sendText(e.getMessage());
    }
  }

  @Override
  public void onBidOffer(BidOffer bidOffer) {

    if (this.session == null) {
      session.getOpenSessions().forEach(openedSession -> {
        try {
          openedSession.getBasicRemote().sendObject(bidOffer);
        } catch (IOException | EncodeException e) {
          e.printStackTrace();
        }
      });
    }
  }
}