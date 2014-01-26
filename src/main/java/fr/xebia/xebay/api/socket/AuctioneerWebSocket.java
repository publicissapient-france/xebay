package fr.xebia.xebay.api.socket;

import com.google.gson.Gson;
import fr.xebia.xebay.api.rest.dto.BidParam;
import fr.xebia.xebay.domain.User;
import fr.xebia.xebay.domain.UserNotAllowedException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@ServerEndpoint(value = "/socket/auctioneer/{authToken}",
        encoders = WebSocketCoder.BidParamEncoder.class,
        decoders = WebSocketCoder.BidParamDecoder.class)
public class AuctioneerWebSocket {
    final Gson gson = new Gson();

    @OnMessage
    public void onMessage(Session session, @PathParam("authToken") String authToken, BidParam bidParam) {

        User user;
        try {
            user = BID_SERVER.users.getUser(authToken);
        } catch (UserNotAllowedException e) {
            return;
        }

        if (bidParam != null) {
            BID_SERVER.bidEngine.bid(user, bidParam.getItemName(), bidParam.getCurValue(), bidParam.getIncrement());
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