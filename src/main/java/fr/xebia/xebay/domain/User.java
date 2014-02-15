package fr.xebia.xebay.domain;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class User implements Principal {
    public static final int INITIAL_BALANCE = 1000;

    private final String key;
    private final String name;
    private final Set<Item> items;

    private double balance;

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

    public double getBalance() {
        return balance;
    }

    void buy(Item item) {
        balance -= item.getValue();
        items.add(item);
    }

    public void sell(Item item) {
        balance += item.getValue();
        items.remove(item);
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
}
