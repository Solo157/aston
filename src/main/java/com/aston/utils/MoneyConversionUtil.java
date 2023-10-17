package com.aston.utils;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class MoneyConversionUtil {

    public static BigDecimal toMoneyBigDecimal(Long value) {
        return new BigDecimal(value).divide(BigDecimal.valueOf(100), 2, HALF_UP);
    }

    public static Long toMoneyLong(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100)).setScale(0, HALF_UP).longValueExact();
    }
}
