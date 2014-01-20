package fr.xebia.xebay.domain;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.IntStream.range;

public class Users {
    private static final String CHARS_IN_KEY = "" +
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "-_";

    private final Set<User> users = new HashSet<>();

    private final Random random;

    public Users() {
        this.random = new Random();
    }

    public User create(String email) throws BidException {
        if (containsEmail(email)) {
            throw new BidException(format("\"%s\" is already registered", email));
        }
        String key;
        do {
            StringBuilder randomKey = new StringBuilder();
            range(0, 16).forEach((i) -> randomKey.append(CHARS_IN_KEY.charAt(random.nextInt(CHARS_IN_KEY.length()))));
            key = randomKey.toString();
        } while (containsKey(key));
        User newUser = new User(key, email);
        users.add(newUser);
        return newUser;
    }

    public void remove(String key, String email) throws UserNotAllowedException, BidException {
        if (!containsEmail(email)) {
            throw new BidException(format("\"%s\" not registered", email));
        }
        User user = getUser(key);
        if (!user.getEmail().equals(email)) {
            throw new BidException(format("\"%s\" registered but bad email", email));
        }
        users.remove(user);
    }

    public User getUser(String key) throws UserNotAllowedException {
        checkUserKey(key);
        return findByKey(key);
    }

    public void checkUserKey(String key) throws UserNotAllowedException {
        if (!containsKey(key)) {
            throw new UserNotAllowedException(format("key \"%s\" is unknown", key));
        }
    }

    private User findByKey(String key) throws NoSuchElementException {
        return users.stream()
                .filter((user) -> user.getKey().equals(key))
                .findFirst()
                .get();
    }

    private boolean containsKey(String key) {
        return users.stream().anyMatch((user) -> user.getKey().equals(key));
    }

    private boolean containsEmail(String email) {
        return users.stream().anyMatch((user) -> user.getEmail().equals(email));
    }
}
