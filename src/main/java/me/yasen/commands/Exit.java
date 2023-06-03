package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.App;
import me.yasen.cli.CliReader;

import java.io.PrintStream;
import java.util.concurrent.Callable;

public class Exit implements Command {

    private final PrintStream printStream;

    @Inject
    public Exit(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public Void call() {
        printStream.println("Goodbye!");
        System.exit(0);

        return null;
    }
}
