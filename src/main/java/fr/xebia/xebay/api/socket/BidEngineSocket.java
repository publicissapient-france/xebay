package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.BidEngineListener;
import fr.xebia.xebay.domain.BidException;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;
import fr.xebia.xebay.domain.internal.Users;
import fr.xebia.xebay.domain.BidOffer;
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
        sessions.add(session);
    }

    @OnMessage
    public BidEngineSocketOutput onMessage(BidEngineSocketInput bidInput, @PathParam("key") String key) {

        BidEngineSocketOutput output = new BidEngineSocketOutput();
        if (bidInput == null) {
            output.addMessage("No messages provided");
            return output;
        }

        User user;
        try {
            user = users.getUser(key);
        } catch (Exception e) {
            output.addMessage("User is unknown");
            return output;
        }

        BidDemand bid = bidInput.getBid();
        if (bid != null) {
            try {
                bidEngine.bid(user, bid.getItemName(), bid.getValue());
            } catch (BidException e) {
                output.addMessage(e.getMessage());
            }
        }

        BidDemand offer = bidInput.getOffer();
        if (offer != null) {
            Item item = items.find(offer.getItemName());
            try {
                bidEngine.offer(user, item, offer.getValue());
            } catch (BidException e) {
                output.addMessage(e.getMessage());
            }
        }

        return output;
    }

    @OnClose
    @OnError
    public void onDisconnect(Session session) {
        sessions.remove(session);
    }

    @Override
    public void onBidOfferStarted(BidOffer startedBidOffer) {
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setStarted(startedBidOffer);
        onBidOffer(output);
    }

    @Override
    public void onBidOfferUpdated(BidOffer updatedBidOffer) {
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setUpdated(updatedBidOffer);
        onBidOffer(output);
    }

    @Override
    public void onBidOfferResolved(BidOffer resolvedBidOffer) {
        BidEngineSocketOutput output = new BidEngineSocketOutput();
        output.setResolved(resolvedBidOffer);
        onBidOffer(output);
    }

    void onBidOffer(BidEngineSocketOutput output) {
        sessions.stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getBasicRemote().sendObject(output);
            } catch (IOException | EncodeException e) {
                log.error("BidInfo notification in error", e);
            }
        });
    }
}