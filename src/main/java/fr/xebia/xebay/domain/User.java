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
    private double balance;
    private Set<Item> items;

    public User(String key, String name) {
        this.name = name;
        this.key = key;

        this.balance = INITIAL_BALANCE;
        this.items = new HashSet<>();
    }


    public double getBalance() {
        return balance;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Set<Item> getItems() {
        return unmodifiableSet(items);
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
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
}
