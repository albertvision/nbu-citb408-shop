package me.yasen.cli;

import me.yasen.validation.Rule;

import java.util.LinkedHashSet;
import java.util.Scanner;

public class StringReader extends BaseReader<String> {
    public StringReader(String fieldName, LinkedHashSet<Rule> rules) {
        super(fieldName, rules);
    }

    @Override
    public String read(Scanner scanner) throws Exception {
        String value = scanner.nextLine().trim();

        if (value.length() == 0) {
            return null;
        }

        return value;
    }
}
