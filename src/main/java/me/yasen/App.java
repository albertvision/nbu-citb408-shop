package me.yasen;

import com.google.inject.Injector;
import com.google.inject.Singleton;
import jakarta.inject.Inject;
import me.yasen.cli.CliReader;
import me.yasen.cli.ObjectFromMapReader;
import me.yasen.commands.*;
import me.yasen.exceptions.Exception;
import me.yasen.models.Product;
import me.yasen.models.Shop;
import me.yasen.validation.Required;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class App {
    private static final List<me.yasen.models.Command> commands = new ArrayList<>() {{
        add(new me.yasen.models.Command("add_product", AddProduct.class));
        add(new me.yasen.models.Command("add_shop", AddShop.class));
        add(new me.yasen.models.Command("add_product_in_shop", AddProductInShop.class));
        add(new me.yasen.models.Command("set_cashier_desk", SetCashierDesk.class));
        add(new me.yasen.models.Command("add_customer", AddCustomer.class));
        add(new me.yasen.models.Command("serve_cash_desk", ServeCashDesk.class));
        add(new me.yasen.models.Command("exit", Exit.class));
    }};

    private final Injector injector;
    private final PrintStream printStream;
    private final ExecutorService executorService;
    private final CliReader cliReader;
    private final ArrayList<Shop> shops = new ArrayList<>();

    private final Map<String, Product> products = new HashMap<>();

    @Inject
    public App(
        Injector injector,
        PrintStream printStream,
        ExecutorService executorService,
        CliReader cliReader
    ) {
        this.injector = injector;
        this.printStream = printStream;
        this.executorService = executorService;
        this.cliReader = cliReader;
    }

    public void run() {
        printStream.println("WELCOME TO THE SHOP APP!");

        Map<String, me.yasen.models.Command> commandsByKey = commands.stream()
            .collect(Collectors.toMap(me.yasen.models.Command::getKey, Function.identity()));

        while (true) {
            me.yasen.models.Command commandKey = cliReader.readField(
                new ObjectFromMapReader<>("command", commandsByKey, new LinkedHashSet<>() {{
                    add(new Required());
                }})
            );

            Command command = injector.getInstance(commandKey.getClassName());

            Future<Void> result = executorService.submit(command);
            try {
                result.get();
            } catch (Exception e) {
                printStream.printf("Error: %s", e.getMessage());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public Map<String, Product> getProducts() {
        return products;
    }
}
