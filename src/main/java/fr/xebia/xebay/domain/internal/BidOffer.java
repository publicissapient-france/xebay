package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.BidException;

import java.math.BigDecimal;
import java.util.Date;

import static fr.xebia.xebay.domain.utils.Math.round;
import static java.lang.Math.max;
import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Locale.ENGLISH;

public class BidOffer {
    private static final BigDecimal MIN_BID_RATIO = ONE.divide(TEN);

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

    public fr.xebia.xebay.domain.BidOffer toBidOffer(boolean isExpired) {
        return new fr.xebia.xebay.domain.BidOffer(item.getCategory(),
                item.getName(),
                round(currentValue),
                getTimeToLive(),
                item.getOwner() == null ? null : item.getOwner().getName(),
                futureBuyer == null ? null : futureBuyer.getName(),
                isExpired,
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

    public boolean isExpired() {
        return getTimeToLive() == 0;
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
        BigDecimal increment = newValue.subtract(initialValue);
        if (currentValue.multiply(MIN_BID_RATIO).compareTo(increment) >= 0) {
            throw new BidException(format(ENGLISH, "increment %.2f$ is less than ten percent of initial value %.2f$ of item \"%s\"", increment, initialValue, item.getName()));
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