package fr.xebia.xebay.domain;

import java.util.Optional;

public class BidOffer {
    public final String itemName;
    public final double initialValue;
    public final double currentValue;
    public final long timeToLive;
    public final Optional<String> ownerEmail;
    public final Optional<String> futureBuyerEmail;

    public BidOffer(Item item, double initialValue, double currentValue, long timeToLive, User futureBuyer) {
        this.timeToLive = timeToLive;
        this.itemName = item.getName();
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.ownerEmail = item.getOwner() == null ? Optional.empty() : Optional.of(item.getOwner().getEmail());
        this.futureBuyerEmail = futureBuyer == null ? Optional.empty() : Optional.of(futureBuyer.getEmail());
    }
}
