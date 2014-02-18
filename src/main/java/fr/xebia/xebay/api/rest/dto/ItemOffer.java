package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.Item;

public class ItemOffer {

    private String name;
    private String category;
    private double value;

    public ItemOffer() {
    }

    public ItemOffer(String category, String itemName, double initialValue) {
        this.category = category;
        this.name = itemName;
        this.value = initialValue;
    }

    public static ItemOffer newItemOffer(Item item) {
        return new ItemOffer(item.getCategory(), item.getName(), item.getValue());
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
