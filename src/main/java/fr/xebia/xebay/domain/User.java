package fr.xebia.xebay.domain;

import fr.xebia.xebay.domain.model.PublicUser;
import fr.xebia.xebay.domain.utils.Gravatar;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

public class User implements Principal {
    public static final int INITIAL_BALANCE = 1000;

    private final String key;
    private final String name;
    private final Set<fr.xebia.xebay.domain.Item> items;
    private final String avatar;

    private double balance;

    public User(String key, String name) {
        this.key = key;
        this.name = name;
        this.items = new HashSet<>();
        this.balance = INITIAL_BALANCE;
        this.avatar = "http://www.gravatar.com/avatar/" + Gravatar.md5Hex(name);
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<fr.xebia.xebay.domain.Item> getItems() {
        return unmodifiableSet(items);
    }

    public double getBalance() {
        return balance;
    }

    void buy(fr.xebia.xebay.domain.Item item) {
        balance -= item.getValue();
        items.add(item);
    }

    public void sell(fr.xebia.xebay.domain.Item item) {
        balance += item.getValue();
        items.remove(item);
    }

    public fr.xebia.xebay.domain.model.User toUser() {
        return new fr.xebia.xebay.domain.model.User(name, key, avatar, getBalance(), items.stream()
                .map(Item::toItem)
                .collect(toSet()));
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

    public boolean canBid(double cost) {
        return this.balance > cost;
    }

    public boolean isInRole(String role) {
        return false;
    }

    public String getAvatar() {
        return avatar;
    }

    public PublicUser toPublicUser() {
        return new PublicUser(name, balance, items.stream().mapToDouble(item -> item.getValue()).sum());
    }
}
