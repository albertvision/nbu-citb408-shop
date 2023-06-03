package me.yasen.cli;

import jakarta.inject.Inject;
import me.yasen.validation.AllRulesValid;

import java.io.PrintStream;
import java.util.Scanner;

public class CliReader {
    private final Scanner scanner;
    private final PrintStream printStream;

    @Inject
    public CliReader(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.printStream = printStream;
    }

    public <T> T readField(Reader<T> reader) {
        T value = null;

        do {
            reader.doPreRead(printStream);
            printStream.println(reader.getRules().toString());

            try {
                value = reader.read(scanner);
            } catch (Exception ignored) {

            }
        } while (!new AllRulesValid(reader.getRules()).isValid(value));

        return value;
    }
}
