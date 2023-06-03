package me.yasen.validation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneOf<T> implements Rule {
    private final Collection<T> allowedValues;
    private Object original = null;

    public OneOf(Collection<T> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public OneOf(Map<String, T> allowedValues) {
        this.allowedValues = allowedValues.values().stream().toList();
        this.original = allowedValues;
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }

        return allowedValues.contains((T)value);
    }

    @Override
    public String toString() {
        if (original instanceof Map<?,?>) {
            return original.toString();
        }

        if (allowedValues instanceof List<T>) {
            return IntStream.range(0, allowedValues.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), it -> ((List<T>) allowedValues).get(it)))
                .toString();
        }

        return allowedValues.toString();
    }
}
