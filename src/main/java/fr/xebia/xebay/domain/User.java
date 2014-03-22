package fr.xebia.xebay.domain;

import java.util.Set;

public class User {
    private String name;
    private String key;
    private String avatarUrl;
    private double balance;
    private Set<Item> items;

    public User(String name, String key, String avatarUrl, double balance, Set<Item> items) {
        this.name = name;
        this.key = key;
        this.avatarUrl = avatarUrl;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "Name " + name + " Balance " + balance;
    }

}
