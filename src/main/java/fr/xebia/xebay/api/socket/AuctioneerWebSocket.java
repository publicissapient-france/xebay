package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.BidServer;
import fr.xebia.xebay.api.rest.dto.BidParam;
import fr.xebia.xebay.domain.BidEngine;
import com.google.gson.Gson;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/socket/auctioneer/{authToken}",
        encoders = WebSocketCoder.BidParamEncoder.class,
        decoders = WebSocketCoder.BidParamDecoder.class)
public class AuctioneerWebSocket {

  final Gson gson = new Gson();

  final BidEngine bidEngine = BidServer.BID_SERVER.bidEngine();

  @OnMessage
  public void onMessage(Session session, @PathParam("authToken") String authToken, BidParam bidParam) {

    // TODO authentication

    if (bidParam != null) {
      // FIXME bidEngine.bid(bidParam.getBuyer(), bidParam.getItemName(), bidParam.getCurValue(), bidParam.getCurValue());
    }

    session.getOpenSessions().forEach(openedSession -> {

      if (openedSession.isOpen()) {
        try {
          openedSession.getBasicRemote().sendText(gson.toJson(bidParam));
        } catch (IOException e) {
          e.printStackTrace(); // FIXME
        }
      }
    });
  }
}