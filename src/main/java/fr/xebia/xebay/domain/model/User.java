package fr.xebia.xebay.domain.model;

import java.util.Set;

public class User {
    private String name;
    private double balance;
    private Set<Item> items;

    public User(String name, double balance, Set<Item> items) {
        this.name = name;
        this.balance = balance;
        this.items = items;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Name " + name + " Balance " + balance;
    }
}
