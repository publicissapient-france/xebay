package fr.xebia.xebay.domain;

public class UserNotAllowedException extends RuntimeException {
    public UserNotAllowedException(String message) {
        super(message);
    }
}
