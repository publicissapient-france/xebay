package bid;

import java.util.Iterator;
import java.util.LinkedHashSet;

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
        return current.increment(name, value, increment, users.findByKey(key));
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
