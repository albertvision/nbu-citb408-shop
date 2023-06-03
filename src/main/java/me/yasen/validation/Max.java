package me.yasen.validation;

import java.math.BigDecimal;

public class Max<T> implements Rule {
    private final T max;
    private final int maxCompareValue;

    public Max(T max) {
        this(max, true);
    }

    public Max(T max, boolean inclusive) {
        this.max = max;
        this.maxCompareValue = inclusive ? 0 : 1;
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof Integer && max instanceof Integer) {
            return ((Integer) value).compareTo((Integer) max) <= maxCompareValue;
        }

        if (value instanceof BigDecimal && max instanceof BigDecimal) {
            return ((BigDecimal) value).compareTo((BigDecimal) max) <= maxCompareValue;
        }

        throw new RuntimeException("Not implemented for this type");
    }

    @Override
    public String toString() {
        String character = maxCompareValue == 0 ? "<=" : "<";

        return String.format("%s%s", character, max);
    }
}
