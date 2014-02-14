package fr.xebia.xebay.api.rest.dto;


import fr.xebia.xebay.domain.Item;
import fr.xebia.xebay.domain.User;

import java.util.HashSet;
import java.util.Set;

public class UserInfo {
    private String name;
    private double balance;
    private Set<ItemOffer> items;

    public UserInfo(String name, double balance, Set<ItemOffer> items) {
        this.name = name;
        this.balance = balance;
        this.items = items;
    }

    public static UserInfo newUserInfo(User user) {
        Set<ItemOffer> userItems = new HashSet<>();
        for(Item item : user.getItems()){
            userItems.add(ItemOffer.newItemOffer(item));
        }
        return new UserInfo(user.getName(), user.getBalance(), userItems);
    }


    public UserInfo() {
    }


    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public Set<ItemOffer> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name ").append(name);
        stringBuilder.append("Balance ").append(balance);
        return stringBuilder.toString();
    }

}
