package me.yasen.validation;

import java.math.BigDecimal;

public class Min<T extends Number> implements Rule {
    private final T min;
    private final int minCompareValue;

    public Min(T min) {
        this(min, true);
    }

    public Min(T min, boolean inclusive) {
        this.min = min;
        this.minCompareValue = inclusive ? 0 : 1;
    }

    public boolean isValid(Object value) {
        if (value instanceof String && min instanceof Integer) {
            return Integer.compare(((String) value).length(), (Integer) this.min) >= minCompareValue;
        }

        if (value instanceof BigDecimal && min instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo((BigDecimal) min) >= minCompareValue;
        }

        if (value instanceof Integer && min instanceof Integer) {
            return ((Integer) value).compareTo((Integer) min) >= minCompareValue;
        }

        throw new RuntimeException("Not implemented for this type");
    }

    @Override
    public String toString() {
        String character = minCompareValue == 0 ? ">=" : ">";

        if (min instanceof Integer) {
            return String.format("%s%d", character, min);
        }

        return String.format("%s%s", character, min);
    }
}
