package bid;

class Item {
    private final String name;
    private double value;

    Item(String name, double value) {
        this.name = name;
        this.value = value;
    }

    String getName() {
        return name;
    }

    double getValue() {
        return value;
    }

    void updateValue(double value) {
        this.value = value;
    }
}
