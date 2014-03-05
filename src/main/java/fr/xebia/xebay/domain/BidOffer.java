package fr.xebia.xebay.domain;

public class BidOffer {
    private Item item;
    private long timeToLive;
    private String userName;
    private String avatarUrl;
    private boolean expired;

    public BidOffer() {
    }

    public BidOffer(String itemCategory, String itemName, double value, long timeToLive, String userName, String avatarUrl, boolean expired) {
        this.item = new Item(itemCategory, itemName, value);
        this.timeToLive = timeToLive;
        this.userName = userName;
        this.expired = expired;
        this.avatarUrl = avatarUrl;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isExpired() {
        return expired;
    }
}
