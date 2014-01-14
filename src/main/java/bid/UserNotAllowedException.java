package bid;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException(String message) {
        super(message);
    }

    UserNotAllowedException() {
    }
}
