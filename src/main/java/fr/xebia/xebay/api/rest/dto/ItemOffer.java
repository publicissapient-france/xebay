package fr.xebia.xebay.api.rest.dto;

public class ItemOffer {

    private String name;
    private double value;

    public ItemOffer() {
    }

    public ItemOffer(String itemName, double initialValue) {
        this.name = itemName;
        this.value = initialValue;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
