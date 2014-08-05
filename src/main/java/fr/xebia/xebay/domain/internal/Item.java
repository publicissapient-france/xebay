package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.Amount;

public class Item {
    public static final User BANK = null;

    private final String category;
    private final String name;

    private Amount value;
    private User owner;

    private boolean offered = false;

    public Item(String category, String name, Amount value) {
        this.category = category;
        this.name = name;
        this.value = value;
        this.owner = BANK;
    }

    public String getName() {
        return name;
    }

    public Amount getValue() {
        return value;
    }

    public User getOwner() {
        return owner;
    }

    public String getCategory() {
        return category;
    }

    public void concludeTransaction(Amount value, User buyer) {
        this.value = value;

        if (owner != null) {
            owner.sell(this);
        }

        if (buyer != BANK) {
            buyer.buy(this);
        }

        offered = false;
        owner = buyer;
    }

    public void depreciate() {
        offered = false;
        value = value.minusTenPercent();
    }

    public boolean isOffered() {
        return offered;
    }

    public void setOffered(){
        offered = true;
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
        return new fr.xebia.xebay.domain.Item(category, name, value.value(), offered);
    }
}
