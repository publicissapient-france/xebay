package fr.xebia.xebay.domain;

public class BidForbiddenException extends RuntimeException {
    public BidForbiddenException(String message) {
        super(message);
    }
}
