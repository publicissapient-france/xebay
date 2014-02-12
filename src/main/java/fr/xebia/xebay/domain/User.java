package fr.xebia.xebay.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class User implements Principal {
    private static final Logger log = LoggerFactory.getLogger("UserInfo");

    public static final int INITIAL_BALANCE = 1000;
    private final String email;
    private final String key;

    private double balance;
    private Set<Item> items;

    public User(String key, String email) {

        this.email = email;
        this.key = key;

        this.balance = INITIAL_BALANCE;
        this.items = new HashSet<>();
        log.debug("User items ", items );

    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
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

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    @Override
    public String toString() {
        return email;
    }

    public boolean canBid(double cost) {
        return this.balance > cost;
    }
}
