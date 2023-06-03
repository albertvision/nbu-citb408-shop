package me.yasen.cli;

import me.yasen.validation.Rule;

import java.io.PrintStream;
import java.util.LinkedHashSet;

public abstract class BaseReader<T> implements Reader<T> {
    protected final String name;
    private final LinkedHashSet<Rule> rules;

    public BaseReader(String name, LinkedHashSet<Rule> rules) {
        this.name = name;
        this.rules = rules;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void doPreRead(PrintStream printStream) {
        printStream.printf("Enter %s: \n", name);
    }

    @Override
    public LinkedHashSet<Rule> getRules() {
        return rules;
    }
}
