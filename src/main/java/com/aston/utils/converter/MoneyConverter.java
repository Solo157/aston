package com.aston.utils.converter;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class MoneyConverter {

    public static BigDecimal toMoneyBigDecimal(Long value) {
        return new BigDecimal(value).divide(BigDecimal.valueOf(100), 2, HALF_UP);
    }

    public static Long toMoneyLong(BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(100)).setScale(0, HALF_UP).longValueExact();
    }
}
