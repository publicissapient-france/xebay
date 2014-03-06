package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidEngineListener;
import fr.xebia.xebay.domain.BidOffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@ServerEndpoint(value = "/socket/bidEngine", encoders = BidOfferEncoder.class)
public class BidEngineSocket implements BidEngineListener {

    static final Logger log = LoggerFactory.getLogger("BidEngineSocket");

    final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    {
        BID_SERVER.bidEngine.addListener(this);
    }

    @OnOpen
    public void onConnect(Session session) {
        sessions.add(session);
    }

    @OnClose
    @OnError
    public void onDisconnect(Session session) {
        sessions.remove(session);
    }

    @Override
    public void onBidOfferUpdated(BidOffer updatedBidOffer) {
        onBidOffer(updatedBidOffer);
    }

    @Override
    public void onBidOfferResolved(BidOffer resolvedBidOffer) {
        onBidOffer(resolvedBidOffer);
    }

    @Override
    public void onBidOfferStarted(BidOffer newBidOffer) {
        onBidOffer(newBidOffer);
    }

    private void onBidOffer(BidOffer bidOffer) {
        sessions.stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getBasicRemote().sendObject(bidOffer);
            } catch (IOException | EncodeException e) {
                log.error("BidInfo notification in error", e);
            }
        });
    }
}