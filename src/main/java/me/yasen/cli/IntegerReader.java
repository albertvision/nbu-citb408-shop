package me.yasen.cli;

import me.yasen.validation.Rule;

import java.util.LinkedHashSet;
import java.util.Scanner;

public class IntegerReader extends BaseReader<Integer> {
    public IntegerReader(String name, LinkedHashSet<Rule> rules) {
        super(name, rules);
    }

    @Override
    public Integer read(Scanner scanner) throws Exception {
        String line = scanner.nextLine().trim();

        if (line.length() == 0) {
            return null;
        }

        return Integer.parseInt(line);
    }
}
