package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.App;
import me.yasen.models.CashDesk;
import me.yasen.models.Cashier;
import me.yasen.models.Shop;
import me.yasen.cli.CliReader;
import me.yasen.cli.ObjectFromList;
import me.yasen.cli.ObjectFromMapReader;
import me.yasen.validation.Required;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetCashierDesk implements Command {
    private final App app;
    private final CliReader cliReader;
    private final PrintStream printStream;

    @Inject
    public SetCashierDesk(App app, CliReader cliReader, PrintStream printStream) {
        this.app = app;
        this.cliReader = cliReader;
        this.printStream = printStream;
    }

    @Override
    public Void call() {
        if (app.getShops().size() < 1) {
            return null;
        }

        Shop shop = cliReader.readField(
            new ObjectFromList<>("index of shop", app.getShops(), new LinkedHashSet<>() {{
                add(new Required());
            }})
        );

        Cashier cashier = cliReader.readField(
            new ObjectFromMapReader<>(
                "cashier ID: ",
                shop.getCashiers().stream().collect(Collectors.toMap(Cashier::getId, Function.identity())),
                new LinkedHashSet<>(){{
                    add(new Required());
                }}
            )
        );

        if (shop.getCashDesks().stream().anyMatch(it -> it.getCashier() == cashier && !it.getQueue().isEmpty())) {
            printStream.println("Cashier already is working on a cash desk which has people waiting on the queue.");
            return null;
        }

        CashDesk cashDesk = cliReader.readField(
            new ObjectFromList<>("closed cash desk", shop.getCashDesks(), new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        shop.getCashDesks().
            stream().
            filter(it -> it.getCashier() == cashier).
            findFirst().
            ifPresent(it -> it.setCashier(null));

        cashDesk.setCashier(cashier);

        return null;
    }
}
