package bid;

public class BidEngine {
    private final Users users;
    private final Items items;

    private BidOffer bidOffer;
    private int tick;

    public BidEngine(Items items, Users users) {
        this.items = items;
        this.users = users;
        this.bidOffer = new BidOffer(this.items.next());
        this.tick = 0;
    }


    public BidOffer currentBidOffer() {
        return bidOffer;
    }

    public BidOffer getBidOffer(String name) {
        return null;
    }


    public BidOffer bid(String key, String name, double value, double increment) throws BidException, UserNotAllowedException {
        return bidOffer.increment(name, value, increment, users.getUser(key));
    }

    void tick() {
        tick++;
        if (tick % 10 == 0) {
            bidOffer.resolve();
            bidOffer = new BidOffer(items.next());
        }
    }

}
