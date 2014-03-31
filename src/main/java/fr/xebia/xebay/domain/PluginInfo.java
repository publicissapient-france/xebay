package fr.xebia.xebay.domain;

public class PluginInfo {
    private String name;
    private boolean activated;
    private String description;

    public PluginInfo(String name, String description, boolean activated) {
        this.name = name;
        this.activated = activated;
        this.description = description;
    }

    public PluginInfo() {
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

        PluginInfo plugin = (PluginInfo) o;

        return name.equals(plugin.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
