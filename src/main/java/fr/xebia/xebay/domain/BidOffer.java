package fr.xebia.xebay.domain;

public class BidOffer {

    private Item item;
    private long timeToLive;
    private String owner;
    private String bidder;
    private boolean expired;

    public BidOffer() {
    }

    public BidOffer(String itemCategory, String itemName, double value, long timeToLive, String owner, String bidder, boolean expired, boolean onSale) {
        this.item = new Item(itemCategory, itemName, value, onSale);
        this.timeToLive = timeToLive;
        this.owner = owner;
        this.bidder = bidder;
        this.expired = expired;
    }

    public Item getItem() {
        return item;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public String getOwner() {
        return owner;
    }

    public String getBidder() {
        return bidder;
    }

    public boolean isExpired() {
        return expired;
    }
}
