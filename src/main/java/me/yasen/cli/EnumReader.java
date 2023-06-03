package me.yasen.cli;

import me.yasen.validation.OneOf;
import me.yasen.validation.Rule;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class EnumReader<T extends Enum<T>> extends BaseReader<T>{

    private final Class<T> enumClass;

    public EnumReader(String name, Class<T> enumClass, LinkedHashSet<Rule> rules) {
        super(name, rules);
        this.enumClass = enumClass;

        rules.add(new OneOf<>(new LinkedHashSet<>(Arrays.asList(enumClass.getEnumConstants()))));
    }

    @Override
    public T read(Scanner scanner) throws Exception {
        return T.valueOf(enumClass, scanner.nextLine().trim());
    }
}
