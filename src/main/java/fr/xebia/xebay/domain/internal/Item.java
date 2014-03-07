package fr.xebia.xebay.domain.internal;

import static java.lang.Math.rint;

public class Item {
    public static final User BANK = null;

    private final String category;
    private final String name;

    private double value;
    private User owner;

    public Item(String category, String name, double value) {
        this.category = category;
        this.name = name;
        this.value = rint(value * 100) / 100;
        this.owner = BANK;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public User getOwner() {
        return owner;
    }

    public String getCategory() {
        return category;
    }

    public void concludeTransaction(double value, User buyer) {
        this.value = value;

        if (owner != null) {
            owner.sell(this);
        }

        if (buyer != BANK) {
            buyer.buy(this);
        }

        owner = buyer;
    }

    public void depreciate() {
        double centValue = value * 100;
        this.value = rint(centValue - (centValue / 10)) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public void backToBank() {
        this.owner = BANK;
    }

    public fr.xebia.xebay.domain.Item toItem() {
        return new fr.xebia.xebay.domain.Item(category, name, value);
    }
}
