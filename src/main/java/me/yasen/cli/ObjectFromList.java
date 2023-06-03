package me.yasen.cli;

import me.yasen.validation.OneOf;
import me.yasen.validation.Rule;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ObjectFromList<T> extends BaseReader<T> {
    private final List<T> list;
    private final IntegerReader integerReader;

    public ObjectFromList(String name, List<T> list, LinkedHashSet<Rule> rules) {
        super(name, new LinkedHashSet<>(){{
            add(new OneOf<T>(list));
            addAll(rules);
        }});
        this.list = list;
        this.integerReader = new IntegerReader(name, new LinkedHashSet<>());
    }

    @Override
    public T read(Scanner scanner) throws Exception {
        Integer index = integerReader.read(scanner);

        if (index == null) {
            return null;
        }

        return list.get(index);
    }
}
