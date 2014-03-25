package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.BidEngineListener;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@ServerEndpoint(value = "/socket/bidEngine/{key}", encoders = BidEngineSocketCoder.class, decoders = BidEngineSocketCoder.class)
public class BidEngineSocket implements BidEngineListener {

    static final Logger log = LoggerFactory.getLogger("BidEngineSocket");

    Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    Items items = BID_SERVER.items;
    Users users = BID_SERVER.users;
    BidEngine bidEngine = BID_SERVER.bidEngine;

    public BidEngineSocket() {
        bidEngine.addListener(this);
    }

    @OnOpen
    public void onConnect(Session session) {
        synchronized (sessions) {
            sessions.add(session);
        }
    }

    @OnMessage
    public BidEngineSocketOutput onMessage(BidEngineSocketInput bidInput, @PathParam("key") String key) {

        if (bidInput == null) {
            BidEngineSocketOutput output = new BidEngineSocketOutput();
            output.setError("Provided message is not valid");
            return output;
        }

        BidDemand bid = bidInput.getBid();
        BidDemand offer = bidInput.getOffer();

        try {

            User user = users.getUser(key);
            if (bid != null) {
                bidEngine.bid(user, bid.getItemName(), bid.getValue());
            } else if (offer != null) {
                Item item = items.find(offer.getItemName());
                bidEngine.offer(user, item, offer.getValue());
            }
            return null;

        } catch (Exception e) {
            BidEngineSocketOutput output = new BidEngineSocketOutput();
            output.setError(e.getMessage());
            return output;
        }
    }

    @OnClose
    @OnError
    public void onDisconnect(Session session) {
        synchronized (sessions) {
            sessions.remove(session);
        }
    }

    public void onInfo(String info) {
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setInfo(info);
        sendOutput(output);
    }

    public void onBidOffer(BidOffer bidOffer) {
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setBidOffer(bidOffer);
        sendOutput(output);
    }

    void sendOutput(BidEngineSocketOutput output) {
        synchronized (sessions) {
            sessions.stream().filter(Session::isOpen).forEach(session -> {
                try {
                    session.getBasicRemote().sendObject(output);
                } catch (IOException | EncodeException e) {
                    log.error("Socket notification in error", e);
                }
            });
        }
    }
}