package fr.xebia.xebay.api.rest.dto;


import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.User;

import java.util.Set;

public class UserInfo {
    private String email;
    private double balance;
    private Set<Item> items;

    public UserInfo(String email, double balance, Set<Item> items) {
        this.email = email;
        this.balance = balance;
        this.items = items;
    }

    public static UserInfo newUserInfo(User user) {
        return new UserInfo(user.getEmail(), user.getBalance(), user.getItems());
    }


    public UserInfo() {
    }


    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public boolean hasMonney(){
        return balance > 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Email ").append(email);
        stringBuilder.append("Balance ").append(balance);
        stringBuilder.append("Items ").append(items.stream().toString());
        return stringBuilder.toString();
    }
}
