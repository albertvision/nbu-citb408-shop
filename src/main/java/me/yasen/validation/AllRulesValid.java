package me.yasen.validation;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AllRulesValid implements Rule{

    private final LinkedHashSet<Rule> rules;

    public AllRulesValid(LinkedHashSet<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean isValid(Object value) {
        LinkedHashSet<Rule> applicableRules = value == null
            ? rules.stream().filter(it -> it instanceof Required).collect(Collectors.toCollection(LinkedHashSet::new))
            : rules;

        for (Rule rule : applicableRules) {
            if (!rule.isValid(value)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        if (rules.isEmpty()) {
            return "";
        }

        return rules.toString();
    }
}
