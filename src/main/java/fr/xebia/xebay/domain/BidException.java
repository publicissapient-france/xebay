package fr.xebia.xebay.domain;

public class BidException extends RuntimeException {
    BidException(String message) {
        super(message);
    }

    BidException() {
    }
}
