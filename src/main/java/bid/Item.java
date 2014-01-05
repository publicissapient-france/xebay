package bid;

import static java.lang.Math.rint;

public class Item {
    private final String name;

    private double value;
    private User owner;

    public Item(String name, double value) {
        this.name = name;
        this.value = rint(value * 100) / 100;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    User getOwner() {
        return owner;
    }

    void concludeTransaction(double value, User buyer) {
        this.value = value;

        if (owner != null) {
            owner.sell(this);
        }

        buyer.buy(this);

        owner = buyer;
    }

    void depreciate() {
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
}
