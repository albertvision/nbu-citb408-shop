package me.yasen;

import com.google.inject.AbstractModule;
import me.yasen.storages.DataStorage;
import me.yasen.storages.FileStorage;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Scanner.class).toInstance(new Scanner(System.in));
        bind(PrintStream.class).toInstance(System.out);
        bind(ExecutorService.class).toInstance(Executors.newSingleThreadExecutor());
        bind(DataStorage.class).to(FileStorage.class);
    }
}
