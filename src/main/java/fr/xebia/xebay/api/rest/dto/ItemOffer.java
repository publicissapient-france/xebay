package fr.xebia.xebay.api.rest.dto;

import fr.xebia.xebay.domain.Item;

public class ItemOffer {

    private String name;
    private double value;

    public ItemOffer() {
    }

    public ItemOffer(String itemName, double initialValue) {
        this.name = itemName;
        this.value = initialValue;
    }

    public static ItemOffer newItemOffer(Item item){
        return new ItemOffer(item.getName(), item.getValue());
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
