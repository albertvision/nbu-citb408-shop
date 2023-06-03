package me.yasen.cli;

import me.yasen.validation.Rule;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class LocalDateReader extends BaseReader<LocalDate> {

    public LocalDateReader(String name, LinkedHashSet<Rule> rules) {
        super(name, rules);
    }

    @Override
    public LocalDate read(Scanner scanner) throws Exception {
        return LocalDate.parse(scanner.nextLine().trim());
    }
}
