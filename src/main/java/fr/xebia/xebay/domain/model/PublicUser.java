package fr.xebia.xebay.domain.model;

import static java.lang.Double.compare;
import static java.lang.String.format;

public class PublicUser {
    private String name;
    private double balance;
    private double assets;

    public PublicUser() {
    }

    public PublicUser(String name, double balance, double assets) {
        if (name == null) {
            throw new NullPointerException("name can't be null when creating PublicUser");
        }
        this.name = name;
        this.balance = balance;
        this.assets = assets;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public double getAssets() {
        return assets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicUser that = (PublicUser) o;

        return name.equals(that.name)
                && compare(that.assets, assets) == 0
                && compare(that.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(assets);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return format("%s %f$ + %f$", name, balance, assets);
    }
}
