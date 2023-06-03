package me.yasen.cli;

import me.yasen.validation.Rule;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class BigDecimalReader extends BaseReader<BigDecimal>  {
    public BigDecimalReader(String name, LinkedHashSet<Rule> rules) {
        super(name, rules);
    }

    @Override
    public BigDecimal read(Scanner scanner) throws Exception {
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine(); // take care of NL char.

        return value;
    }
}
