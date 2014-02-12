package fr.xebia.xebay.api.rest.dto;


import fr.xebia.xebay.domain.User;

public class UserInfo {
    private String name;
    private double balance;

    public UserInfo(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public static UserInfo newUserInfo(User user) {
        return new UserInfo(user.getName(), user.getBalance());
    }


    public UserInfo() {
    }


    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name ").append(name);
        stringBuilder.append("Balance ").append(balance);
        return stringBuilder.toString();
    }
}
