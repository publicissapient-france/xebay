package fr.xebia.xebay.api.socket;

import fr.xebia.xebay.domain.BidOffer;

import java.util.ArrayList;
import java.util.List;

public class BidEngineSocketOutput {

    List<String> messages = new ArrayList<>();

    BidOffer started;

    BidOffer updated;

    BidOffer resolved;

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public BidOffer getStarted() {
        return started;
    }

    public void setStarted(BidOffer started) {
        this.started = started;
    }

    public BidOffer getUpdated() {
        return updated;
    }

    public void setUpdated(BidOffer bidded) {
        this.updated = bidded;
    }

    public BidOffer getResolved() {
        return resolved;
    }

    public void setResolved(BidOffer resolved) {
        this.resolved = resolved;
    }
}
