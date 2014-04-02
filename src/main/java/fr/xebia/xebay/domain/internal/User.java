package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.PublicUser;

import javax.security.auth.Subject;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public class User implements Principal {
    public static final BigDecimal INITIAL_BALANCE = new BigDecimal(1000).setScale(2, java.math.RoundingMode.HALF_UP);

    private final String key;
    private final String name;
    private final Set<Item> items;

    private BigDecimal balance;

    private boolean mailed;

    public User(String key, String name) {
        this.key = key;
        this.name = name;
        this.items = new HashSet<>();
        this.balance = INITIAL_BALANCE;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<Item> getItems() {
        return unmodifiableSet(items);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void buy(Item item) {
        balance = balance.subtract(item.getValue());
        items.add(item);
    }

    public void sell(Item item) {
        balance = balance.add(item.getValue());
        items.remove(item);
    }

    public fr.xebia.xebay.domain.User toUser() {
        fr.xebia.xebay.domain.User user = new fr.xebia.xebay.domain.User(name, key, balance.doubleValue(), items.stream()
                .map(Item::toItem)
                .collect(toSet()));
        user.setMailed(mailed);
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return key.equals(user.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean canBid(BigDecimal cost) {
        return balance.compareTo(cost) > 0;
    }

    public boolean isInRole(String role) {
        return false;
    }

    public PublicUser toPublicUser() {
        return new PublicUser(name, balance.doubleValue(), items.stream()
                .map(Item::getValue)
                .reduce(ZERO, (a, b) -> a.add(b)).doubleValue());
    }

    public void credit(BigDecimal value) {
        balance = balance.add(value.abs());
    }

    public void setMailed(boolean mailed) {
        this.mailed = mailed;
    }

    public boolean isMailed() {
        return mailed;
    }
}
