package bid;

import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

class BidServer {
    private final Users users;

    private Iterator<Item> itemsIterator;
    private BidOffer current;
    private int tick;

    @Deprecated
    BidServer(BidOffer... bidOffers) throws BidException {
        this();
        if (bidOffers.length == 0) {
            throw new BidException();
        }
        this.itemsIterator = new HashSet<>(asList(bidOffers).stream().map((bidOffer) -> bidOffer.getItem()).collect(Collectors.toList())).iterator();
        this.current = new BidOffer(itemsIterator.next());
    }

    BidServer(Item... items) {
        this();
        if (items.length == 0) {
            throw new BidException();
        }
        this.itemsIterator = asList(items).iterator();
        this.current = new BidOffer(itemsIterator.next());
    }

    private BidServer() {
        this.users = new Users();
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
            current = new BidOffer(itemsIterator.next());
        }
    }

    User user(String key) {
        return users.findByKey(key);
    }
}
