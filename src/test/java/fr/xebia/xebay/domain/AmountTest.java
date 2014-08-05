package fr.xebia.xebay.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AmountTest {
    @Test
    public void should_store_exact_value_if_cents_are_lower_than_100() {
        Amount amount = new Amount(4.32);

        double exactValue = amount.value();

        assertThat(exactValue).isEqualTo(4.32);
    }

    @Test
    public void should_store_rounded_value_if_cents_are_higher_than_100() {
        Amount amount = new Amount(4.329);

        double exactValue = amount.value();

        assertThat(exactValue).isEqualTo(4.33);
    }

    @Test
    public void should_depreciate() {
        Amount initialAmount = new Amount(4.32);

        Amount amountMinusTenPercent = initialAmount.minusTenPercent();

        assertThat(amountMinusTenPercent.value()).isEqualTo(3.89);
    }

    @Test
    public void should_add() {
        Amount initialAmount = new Amount(4.32);

        Amount addedAmount = initialAmount.add(new Amount(5.22));

        assertThat(addedAmount.value()).isEqualTo(9.54);
    }

    @Test
    public void should_subtract() {
        Amount initialAmount = new Amount(4.32);

        Amount subtractedAmount = initialAmount.subtract(new Amount(5.22));

        assertThat(subtractedAmount.value()).isEqualTo(-0.9);
    }

    @Test
    public void should_get_min_increment() {
        Amount initialAmount = new Amount(4.32);

        Amount minIncrement = initialAmount.minIncrement();

        assertThat(minIncrement.value()).isEqualTo(0.43);
    }

    @Test
    public void should_tell_if_an_amount_is_lower_than_another() {
        Amount lowerAmount = new Amount(4.32);
        Amount higherAmount = new Amount(5.02);

        int difference = lowerAmount.compareTo(higherAmount);

        assertThat(difference).isNegative();
    }

    @Test
    public void should_tell_if_two_amounts_are_equals() {
        Amount amount = new Amount(4.32);
        Amount sameAmount = new Amount(4.32);

        boolean amountsAreEquals = amount.equals(sameAmount);

        assertThat(amountsAreEquals).isTrue();
    }
}
