package bid;

import static java.lang.Math.rint;

class Item {
    private final String name;
    private double value;

    Item(String name, double value) {
        this.name = name;
        this.value = rint(value * 100) / 100;
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

    void depreciate() {
        double centValue = value * 100;
        this.value = rint(centValue - (centValue / 10)) / 100;
    }
}
