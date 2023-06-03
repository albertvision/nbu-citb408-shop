package me.yasen.validation;

public class Required implements Rule {
    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String && ((String) value).length() == 0) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "required";
    }
}
