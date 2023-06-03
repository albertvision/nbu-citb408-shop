package me.yasen;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());

        App app = injector.getInstance(App.class);
        app.run();
    }
}