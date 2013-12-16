package bid;

class User {
    private final String email;
    private final String key;

    private double balance;

    User(String email, String key) {
        this.email = email;
        this.key = key;

        this.balance = 1000;
    }

    double getBalance() {
        return balance;
    }

    String getEmail() {
        return email;
    }

    String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
