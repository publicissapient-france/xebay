package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.Amount;
import fr.xebia.xebay.domain.PublicUser;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static fr.xebia.xebay.domain.Amount.ZERO;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public class User implements Principal {
    public static final Amount INITIAL_BALANCE = new Amount(1000);

    private final String key;
    private final String name;
    private final Set<Item> items;

    private Amount balance;

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

    public Amount getBalance() {
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
        fr.xebia.xebay.domain.User user = new fr.xebia.xebay.domain.User(name, key, getBalance().value(), items.stream()
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

    public boolean canBid(Amount cost) {
        return balance.compareTo(cost) > 0;
    }

    public boolean isInRole(String role) {
        return false;
    }

    public PublicUser toPublicUser() {
        return new PublicUser(name, balance.value(), items.stream()
            .map(item -> item.getValue())
            .reduce(ZERO, (a, b) -> a.add(b)).value());
    }

    public void credit(Amount value) {
        balance = balance.add(value);
    }

    public void setMailed(boolean mailed) {
        this.mailed = mailed;
    }

    public boolean isMailed() {
        return mailed;
    }
}
