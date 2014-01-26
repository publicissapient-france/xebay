package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.socket.coder.BidCallDecoder;
import fr.xebia.xebay.api.socket.coder.BidAnswerEncoder;
import fr.xebia.xebay.api.socket.dto.BidAnswer;
import fr.xebia.xebay.api.socket.dto.BidCall;
import fr.xebia.xebay.domain.*;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@ServerEndpoint(value = "/socket/bidEngine/{authToken}", decoders = BidCallDecoder.class, encoders = BidAnswerEncoder.class)
public class BidEngineSocket implements BidEngineListener {

  static final Logger log = Logger.getLogger("BidEngineSocket");

  private Session session = null;

  @OnOpen
  public void onOpen(Session session) {
    if (this.session == null) {
      this.session = session;
      BID_SERVER.bidEngine.addListener(this);
    }
  }

  @OnMessage
  public void onMessage(Session session, @PathParam("authToken") String authToken, BidCall bidCall) throws IOException, EncodeException {

    BidAnswer bidAnswer;
    try {

      User user = BID_SERVER.users.getUser(authToken);
      BID_SERVER.bidEngine.bid(user, bidCall.getItemName(), bidCall.getCurValue(), bidCall.getIncrement());
      bidAnswer = BidAnswer.newSuccess(bidCall);

    } catch (UserNotAllowedException e) {
      bidAnswer = BidAnswer.newFailure("Invalid key", bidCall);
      log.log(Level.SEVERE, "Invalid key", e);
    } catch (BidException e) {
      bidAnswer = BidAnswer.newFailure("Invalid call", bidCall);
      log.log(Level.SEVERE, "Invalid call", e);
    }
    session.getBasicRemote().sendObject(bidAnswer);
  }

  @Override
  public void onBidOffer(BidOffer bidOffer) {
    if (this.session != null) {
      BidAnswer bidAnswer = BidAnswer.newInfo(bidOffer);
      session.getOpenSessions().forEach(openedSession -> {
        try {
          openedSession.getBasicRemote().sendObject(bidAnswer);
        } catch (IOException | EncodeException e) {
          log.log(Level.SEVERE, "BidInfo notification in error", e);
        }
      });
    }
  }
}