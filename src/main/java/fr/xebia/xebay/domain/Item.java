package fr.xebia.xebay.domain;

public class Item {
    private String name;
    private String category;
    private double value;

    public Item() {

    }
    
    public Item(String category, String name, double initialValue) {
        this.category = category;
        this.name = name;
        this.value = initialValue;
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
}
