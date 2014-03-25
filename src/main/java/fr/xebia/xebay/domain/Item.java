package fr.xebia.xebay.domain;

public class Item {
    private String name;
    private String category;
    private double value;
    private boolean offered;

    public Item() {

    }
    
    public Item(String category, String name, double initialValue, boolean offered) {
        this.category = category;
        this.name = name;
        this.value = initialValue;
        this.offered = offered;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getValue() {
        return value;
    }

    public boolean isOffered() {
        return offered;
    }
}
