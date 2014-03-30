package fr.xebia.xebay.domain;

public class Plugin {
    private String name;
    private boolean activated;
    private String description;

    public Plugin(String name, String description, boolean activated) {
        this.name = name;
        this.activated = activated;
        this.description = description;
    }

    public Plugin() {
    }

    public String getName() {
        return name;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getDescription() {
        return description;
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
