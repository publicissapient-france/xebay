package fr.xebia.xebay.domain.internal;

public class SerializedUser {
    private String key;
    private String name;

    public SerializedUser() {
    }

    public SerializedUser(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
