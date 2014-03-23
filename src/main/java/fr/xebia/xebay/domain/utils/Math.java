package fr.xebia.xebay.domain.utils;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class Math {
    public static double round(BigDecimal value) {
        return value.setScale(2, HALF_UP).doubleValue();
    }

    public static BigDecimal round(double value) {
        return new BigDecimal(value).setScale(2, HALF_UP);
    }

    public static boolean areEquals(BigDecimal a, BigDecimal b) {
        return a.setScale(2, HALF_UP).equals(b.setScale(2, HALF_UP));
    }
}
