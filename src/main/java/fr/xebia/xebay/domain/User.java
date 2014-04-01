package fr.xebia.xebay.domain;

import java.util.Set;

public class User {
    private String name;
    private String key;
    private double balance;
    private Set<Item> items;
    private boolean mailed;

    public User(String name, String key, double balance, Set<Item> items) {
        this.name = name;
        this.key = key;
        this.balance = balance;
        this.items = items;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public double getBalance() {
        return balance;
    }

    public Set<Item> getItems() {
        return items;
    }

    public boolean isMailed() {
        return mailed;
    }

    public void setMailed(boolean mailed) {
        this.mailed = mailed;
    }

    @Override
    public String toString() {
        return "Name " + name + " Balance " + balance;
    }

}
