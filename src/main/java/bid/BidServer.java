package bid;

public enum BidServer {
    BID_SERVER;

    private BidEngine bidEngine;

    BidServer() {
        this.bidEngine = new BidEngine(Items.load("items").get(), new Users());
    }

    public BidEngine bidEngine() {
        return bidEngine;
    }
}
