package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.BidException;

import java.math.BigDecimal;
import java.util.Date;

import static fr.xebia.xebay.domain.utils.Math.round;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public class BidOffer {
    private static final BigDecimal MIN_BID_RATIO = new BigDecimal(".09"); // TODO reintroduce ten percent

    public final Item item;
    public final BigDecimal initialValue;

    private final long created;
    private final long initialTimeToLive;

    BigDecimal currentValue;

    private User futureBuyer;

    public BidOffer(Item item, long initialTimeToLive) {
        this(item, item.getValue(), initialTimeToLive);
    }

    public BidOffer(Item item, BigDecimal initialValue, long initialTimeToLive) {
        this.item = item;
        this.initialValue = initialValue;
        this.initialTimeToLive = initialTimeToLive;
        this.created = new Date().getTime();
        this.currentValue = initialValue;
    }

    public fr.xebia.xebay.domain.BidOffer toBidOffer() {
        return new fr.xebia.xebay.domain.BidOffer(item.getCategory(),
                item.getName(),
                round(currentValue),
                getTimeToLive(),
                item.getOwner() == null ? null : item.getOwner().getName(),
                futureBuyer == null ? null : futureBuyer.getName(),
                item.isOffered());
    }

    public void resolve() {
        if (futureBuyer == null) {
            item.depreciate();
        } else {
            item.concludeTransaction(currentValue, futureBuyer);
        }
        currentValue = item.getValue();
    }

    public long getTimeToLive() {
        long millisecondsSinceCreated = new Date().getTime() - created;
        return max(0, initialTimeToLive - millisecondsSinceCreated);
    }

    public void userIsUnregistered(User user) {
        if (user == futureBuyer) {
            futureBuyer = null;
        }
    }

    public BidOffer bid(String name, BigDecimal newValue, User user) throws BidException {
        checkUser(user);
        if (!item.getName().equals(name)) {
            throw new BidException(format("current item to bid is not \"%s\"", name));
        }
        BigDecimal minIncrement = initialValue.multiply(MIN_BID_RATIO);
        BigDecimal increment = newValue.subtract(currentValue);
        if (increment.compareTo(minIncrement) < 0) {
            throw new BidException(format(ENGLISH, "you have bid %.2f$ which is less than %.2f$ (current value %.2f$ + ten percent of initial value %.2f$)",
                    newValue, currentValue.add(minIncrement), currentValue, initialValue));
        }

        if (!user.canBid(newValue)) {
            throw new BidException(format(ENGLISH, "user can't bid %.2f$, not enought money left.", newValue));
        }

        currentValue = newValue;
        futureBuyer = user;
        return this;
    }

    private void checkUser(User user) {
        if (null == user || (null == user.getName())) {
            throw new BidException("bad user");
        }
    }
}