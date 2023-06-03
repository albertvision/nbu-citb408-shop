package me.yasen.cli;

import me.yasen.validation.OneOf;
import me.yasen.validation.Rule;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;

public class ObjectFromMapReader<T> extends BaseReader<T> {
    private final Map<String, T> map;
    private final StringReader stringReader;

    public ObjectFromMapReader(String name,
                               Map<String, T> map,
                               LinkedHashSet<Rule> rules
    ) {
        super(name, new LinkedHashSet<>() {{
            add(new OneOf<>(map.values()));
            addAll(rules);
        }});

        this.map = map;
        this.stringReader = new StringReader(name, new LinkedHashSet<>());
    }

    @Override
    public T read(Scanner scanner) throws Exception {
        String key = stringReader.read(scanner);

        return map.get(key);
    }
}
