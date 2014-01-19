package bid.domain;

import java.util.logging.Logger;

public class BidEngine {
    private static final Logger log = Logger.getLogger("BidEngine");

    private final Items items;

    private BidOffer bidOffer;
    private int tick;

    public BidEngine(Items items) {
        this.items = items;
        this.bidOffer = new BidOffer(this.items.next());
        this.tick = 0;
    }


    public BidOffer currentBidOffer() {
        log.info("currentBidOffer " + bidOffer.toString());
        return bidOffer;
    }

    public BidOffer getBidOffer(String name) {
        return null;
    }


    public BidOffer bid(User user, String name, double value, double increment) throws BidException, UserNotAllowedException {
        return bidOffer.increment(name, value, increment, user);
    }

    void tick() {
        tick++;
        if (tick % 10 == 0) {
            bidOffer.resolve();
            bidOffer = new BidOffer(items.next());
        }
    }

}
