package fr.xebia.xebay.domain;

public class Plugin {
    private String name;
    private boolean activated;

    public Plugin(String name, boolean activated) {
        this.name = name;
        this.activated = activated;
    }

    public Plugin() {
    }

    public String getName() {
        return name;
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plugin plugin = (Plugin) o;

        return name.equals(plugin.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
