package fr.xebia.xebay.domain;

public class AdminUser extends User {
    public static final String KEY = "4dm1n";
    public static final String ADMIN_ROLE = "admin";

    public AdminUser() {
        super(KEY, "admin");
    }

    @Override
    public double getBalance() {
        return 0d;
    }

    @Override
    void buy(Item item) {
        throw new RuntimeException("admin can't buy");
    }

    @Override
    public void sell(Item item) {
        throw new RuntimeException("admin can't sell");
    }

    @Override
    public boolean isInRole(String role) {
        return ADMIN_ROLE.equals(role);
    }
}
