package bid.domain;

public class BidException extends RuntimeException {
    BidException(String message) {
        super(message);
    }

    BidException() {
    }
}
