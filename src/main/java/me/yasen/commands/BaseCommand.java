package me.yasen.commands;

import me.yasen.App;
import me.yasen.cli.CliReader;

import java.util.concurrent.Callable;

public abstract class BaseCommand implements Callable<Void> {
    protected final App app;

    public BaseCommand(App app) {
        this.app = app;
    }
}
