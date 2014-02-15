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

    private final Set<User> users;

    private final Random random;

    public Users() {
        this.random = new Random();
        this.users = new HashSet<>();
        this.users.add(new AdminUser());
    }

    public User create(String name) throws BidException {
        if (name == null || name.isEmpty()) {
            throw new BidException("can't create user without name");
        }
        if (containsName(name)) {
            throw new BidException(format("\"%s\" is already registered", name));
        }
        String key;
        do {
            StringBuilder randomKey = new StringBuilder();
            range(0, 16).forEach((i) -> randomKey.append(CHARS_IN_KEY.charAt(random.nextInt(CHARS_IN_KEY.length()))));
            key = randomKey.toString();
        } while (containsKey(key));
        User newUser = new User(key, name);
        users.add(newUser);
        return newUser;
    }

    public void remove(String key, String name) throws UserNotAllowedException, BidException {
        if (!containsName(name)) {
            throw new BidException(format("\"%s\" is not registered", name));
        }
        User user = getUser(key);
        if (!user.getName().equals(name)) {
            throw new BidException(format("\"%s\" is registered but bad name", name));
        }
        users.remove(user);
    }

    public User getUser(String key) throws UserNotAllowedException {
        checkUserKey(key);
        return findByKey(key);
    }

    private void checkUserKey(String key) throws UserNotAllowedException {
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

    private boolean containsName(String name) {
        return users.stream().anyMatch((user) -> user.getName().equals(name));
    }
}
