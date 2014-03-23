package fr.xebia.xebay.domain.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MathTest {
    @Test
    public void should_round_from_BigDecimal_to_double() {
        double rounded = Math.round(new BigDecimal(384.3084));

        assertThat(rounded).isEqualTo(384.31);
    }

    @Test
    public void should_round_from_double_to_BigDecimal() {
        BigDecimal rounded = Math.round(384.3084);

        assertThat(rounded.doubleValue()).isEqualTo(384.31);
    }

    @Test
    public void should_be_equals_by_rounding() {
        boolean areEquals = Math.areEquals(new BigDecimal(384.3084), new BigDecimal(384.314));

        assertThat(areEquals).isTrue();
    }
}
