package bid;

import java.util.Iterator;

import static java.util.Arrays.asList;

class BidServer {
    private final Users users;

    private Iterator<Item> itemsIterator;
    private BidOffer current;
    private int tick;

    BidServer(Item... items) {
        if (items.length == 0) {
            throw new BidException();
        }
        this.users = new Users();
        this.itemsIterator = asList(items).iterator();
        this.current = new BidOffer(itemsIterator.next());
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
