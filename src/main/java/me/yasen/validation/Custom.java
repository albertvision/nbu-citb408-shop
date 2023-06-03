package me.yasen.validation;

import java.util.function.Predicate;

public class Custom<T> implements Rule {
    private final Predicate<T> predicate;

    public Custom(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean isValid(Object value)
    {
        if (value == null) {
            return false;
        }

        return predicate.test((T) value);
    }

}
