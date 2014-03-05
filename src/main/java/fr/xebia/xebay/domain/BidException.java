package fr.xebia.xebay.domain;

public class BidException extends RuntimeException {
    public BidException(String message) {
        super(message);
    }

    public BidException() {
    }
}
