package fr.xebia.xebay.domain.model;

public class BidOffer {
    private Item item;
    private long timeToLive;
    private String userName;
    private Boolean expired;

    public BidOffer() {
    }

    public BidOffer(String itemCategory, String itemName, double value, long timeToLive, String userName, boolean expired) {
        this.item = new Item(itemCategory, itemName, value);
        this.timeToLive = timeToLive;
        this.userName = userName;
        this.expired = expired;
    }

    public Item getItem() {
        return item;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getExpired() {
        return expired;
    }
}
