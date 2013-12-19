package bid;

import java.util.Iterator;
import java.util.LinkedHashSet;

import static java.lang.String.format;
import static java.util.Arrays.asList;

class BidServer {
    private final Users users;
    private final Iterator<BidOffer> bidOffersIterator;

    private BidOffer current;
    private int tick;

    BidServer(BidOffer... bidOffers) throws BidException {
        if (bidOffers.length == 0) {
            throw new BidException();
        }
        this.users = new Users();
        this.bidOffersIterator = new LinkedHashSet<>(asList(bidOffers)).iterator();
        this.current = bidOffersIterator.next();
        this.tick = 0;
    }

    String register(String email) throws BidException {
        return users.create(email).getKey();
    }

    BidOffer currentBidOffer(String key) {
        users.findByKey(key);
        return current;
    }

    BidOffer bid(String key, String name, double value, double increment) throws BidException {
        User user = users.findByKey(key);
        if (!current.getItem().getName().equals(name)) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        if (current.getCurrentValue() != value) {
            throw new BidException(format("value for \"%s\" is not %s but %s", current.getItem().getName(), Double.toString(value), Double.toString(current.getCurrentValue())));
        }
        if (current.getCurrentValue() / 10 > increment) {
            throw new BidException(format("increment %s is less than ten percent of initial value %s of item \"%s\"", Double.toString(increment), Double.toString(current.getItem().getValue()), current.getItem().getName()));
        }
        return current.increment(increment, user);
    }

    void tick() {
        tick++;
        if (tick % 10 == 0) {
            current.resolve();
            current = bidOffersIterator.next();
        }
    }

    User user(String key) {
        return users.findByKey(key);
    }
}
