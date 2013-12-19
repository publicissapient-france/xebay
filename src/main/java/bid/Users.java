package bid;

import java.util.HashSet;
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

    private final Random random;
    private final Set<User> users;

    public Users() {
        this.users = new HashSet<>();
        this.random = new Random();
    }

    public User create(String email) {
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

    public User findByKey(String key) throws BidException {
        return users.stream()
                .filter((user) -> user.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new BidException(format("key \"%s\" is unknown", key)));
    }

    private boolean containsKey(String key) {
        return users.stream().anyMatch((user) -> user.getKey().equals(key));
    }

    private boolean containsEmail(String email) {
        return users.stream().anyMatch((user) -> user.getEmail().equals(email));
    }
}
