package bid;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

class User {
    private final String email;
    private final String key;

    private double balance;
    private Set<Item> items;

    User(String key, String email) {
        this.email = email;
        this.key = key;

        this.balance = 1000;
        this.items = new HashSet<>();
    }

    double getBalance() {
        return balance;
    }

    String getEmail() {
        return email;
    }

    String getKey() {
        return key;
    }

    Set<Item> getItems() {
        return unmodifiableSet(items);
    }

    void buy(Item item) {
        balance -= item.getValue();
        items.add(item);
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
}
