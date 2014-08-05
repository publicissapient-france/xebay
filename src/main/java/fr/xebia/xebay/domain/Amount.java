package fr.xebia.xebay.domain;

public class Amount implements Comparable<Amount> {
    public static final Amount ZERO = new Amount(0);
    private final long value;

    public Amount(double value) {
        this.value = Math.round(value * 100);
    }

    private Amount(long value) {
        this.value = value;
    }

    public double value() {
        return (double) value / 100;
    }

    public Amount minusTenPercent() {
        return new Amount(value - value / 10);
    }

    public Amount minIncrement() {
        return new Amount(value / 10);
    }

    public Amount add(Amount amountToAdd) {
        return new Amount(value + amountToAdd.value);
    }

    public Amount subtract(Amount amountToSubtract) {
        return new Amount(value - amountToSubtract.value);
    }

    @Override
    public int compareTo(Amount other) {
        return Long.valueOf(value).compareTo(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Amount amount = (Amount) o;

        return value == amount.value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }
}

