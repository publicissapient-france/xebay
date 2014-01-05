package bid;

import static java.lang.String.format;

public class BidEngine {
    private final Users users;
    private final Items items;

    private BidOffer bidOffer;
    private int tick;

    BidEngine(Items items) {
        this.items = items;
        this.users = new Users();
        this.bidOffer = new BidOffer(this.items.next());
        this.tick = 0;
    }

    public String register(String email) throws BidException {
        return users.create(email).getKey();
    }
    public void unregister(String key, String email) throws BidException {
        users.remove(key, email);
    }


    public BidOffer currentBidOffer() {
        return bidOffer;
    }

    public BidOffer getBidOffer(String name) {
        return null;
    }


    BidOffer bid(String key, String name, double value, double increment) throws BidException {
        return bidOffer.increment(name, value, increment, user(key));
    }

    void tick() {
        tick++;
        if (tick % 10 == 0) {
            bidOffer.resolve();
            bidOffer = new BidOffer(items.next());
        }
    }

    User user(String key) {
        checkUserKey(key);
        return users.findByKey(key);
    }

    private void checkUserKey(String key) throws BidException {
        if (!users.containsKey(key)) {
            throw new BidException(format("key \"%s\" is unknown", key));
        }
    }
}
