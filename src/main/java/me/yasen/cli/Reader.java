package me.yasen.cli;

import me.yasen.validation.Rule;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Scanner;

public interface Reader<T> {
    void doPreRead(PrintStream printStream);

    T read(Scanner scanner) throws Exception;

    LinkedHashSet<Rule> getRules();

    String getName();
}
