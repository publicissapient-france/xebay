package fr.xebia.xebay.domain.internal;

import fr.xebia.xebay.domain.Amount;

import static fr.xebia.xebay.domain.Amount.ZERO;

public class AdminUser extends User {
    public static final String KEY = "4dm1n";
    public static final String ADMIN_ROLE = "admin";

    public AdminUser() {
        super(KEY, "admin");
    }

    @Override
    public Amount getBalance() {
        return ZERO;
    }

    @Override
    public void buy(Item item) {
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

    public static boolean isAdmin(SerializedUser user) {
        return ("admin".equals(user.getName())) && KEY.equals(user.getKey());
    }
}
