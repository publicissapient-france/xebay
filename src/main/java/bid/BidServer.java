package bid;

class BidServer {
    private final Users users;
    private final Items items;

    private BidOffer current;
    private int tick;

    BidServer(Items items) {
        this.items = items;
        this.users = new Users();
        this.current = new BidOffer(this.items.next());
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
            current = new BidOffer(items.next());
        }
    }

    User user(String key) {
        return users.findByKey(key);
    }
}
