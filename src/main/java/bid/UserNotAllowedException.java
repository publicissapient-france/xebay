package bid;

public class UserNotAllowedException extends RuntimeException {
    UserNotAllowedException(String message) {
        super(message);
    }

    UserNotAllowedException() {
    }
}
