package bid;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.IntStream.range;

class BidServer {
    private static final String CHARS_IN_KEY = "" +
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "-_";

    private final HashMap<String, User> authorized;
    private final Iterator<BidOffer> bidOffersIterator;
    private final Random random;

    private BidOffer current;

    BidServer(BidOffer... bidOffers) throws BidException {
        if (bidOffers.length == 0) {
            throw new BidException();
        }
        this.authorized = new HashMap<>();
        this.bidOffersIterator = new LinkedHashSet<>(asList(bidOffers)).iterator();
        this.random = new Random();
        this.current = bidOffersIterator.next();
    }

    String register(String email) throws BidException {
        if (authorized.values().stream().anyMatch((user) -> user.getEmail().equals(email))) {
            throw new BidException(format("\"%s\" is already registered", email));
        }
        StringBuilder key = new StringBuilder();
        do {
            range(0, 16).forEach((i) -> key.append(CHARS_IN_KEY.charAt(random.nextInt(CHARS_IN_KEY.length()))));
        } while (authorized.containsKey(key.toString()));
        authorized.put(key.toString(), new User(email, key.toString()));
        return key.toString();
    }

    BidOffer currentBidOffer(String key) {
        checkKey(key);
        return current;
    }

    BidOffer bid(String key, String name, double value, double increment) throws BidException {
        checkKey(key);
        if (!current.getName().equals(name)) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        if (current.getCurrentValue() != value) {
            throw new BidException(format("value for \"%s\" is not %s but %s", current.getName(), Double.toString(value), Double.toString(current.getCurrentValue())));
        }
        if (current.getCurrentValue() / 10 > increment) {
            throw new BidException(format("increment %s is less than ten percent of initial value %s of item \"%s\"", Double.toString(increment), Double.toString(current.getInitialValue()), current.getName()));
        }
        return current.increment(increment);
    }

    User user(String key) {
        checkKey(key);
        return authorized.get(key);
    }

    private void checkKey(String key) {
        if (!authorized.containsKey(key)) {
            throw new BidException(format("key \"%s\" is unknown", key));
        }
    }
}
