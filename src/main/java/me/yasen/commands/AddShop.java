package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.*;
import me.yasen.cli.BigDecimalReader;
import me.yasen.cli.CliReader;
import me.yasen.cli.IntegerReader;
import me.yasen.cli.StringReader;
import me.yasen.models.CashDesk;
import me.yasen.models.Cashier;
import me.yasen.models.Category;
import me.yasen.models.Shop;
import me.yasen.validation.Min;
import me.yasen.validation.Required;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Period;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddShop implements Command {

    private final App app;
    private final PrintStream printStream;
    private final CliReader cliReader;

    @Inject
    public AddShop(App app, PrintStream printStream, CliReader cliReader) {
        this.app = app;
        this.printStream = printStream;
        this.cliReader = cliReader;
    }

    @Override
    public Void call() throws Exception {
        if (app.getProducts().size() == 0) {
            printStream.println("Please add products first. ");
            return null;
        }

        Map<Category, BigDecimal> categoryMargins = Arrays.stream(Category.values())
            .collect(Collectors.toMap(Function.identity(), it -> {
                BigDecimal categoryMargin = cliReader.readField(
                    new BigDecimalReader(
                        String.format("margin (%%) for %s products", it.name()),
                        new LinkedHashSet<>() {{
                            add(new Required());
                            add(new Min<>(BigDecimal.ZERO));
                        }}
                    )
                );

                return categoryMargin.divide(BigDecimal.valueOf(100));
            }));

        BigDecimal expiryDiscount = cliReader
            .readField(
                new BigDecimalReader("expiry discount (%)", new LinkedHashSet<>(){{
                    add(new Required());
                    add(new Min<>(BigDecimal.ZERO));
                }})
            )
            .divide(BigDecimal.valueOf(100));

        Period expiryDiscountPeriodInDays = Period.ofDays(
            cliReader.readField(
                new IntegerReader("days before expiry date to apply discount", new LinkedHashSet<>(){{
                    add(new Required());
                    add(new Min<>(0));
                }})
            )
        );

        Shop shop = new Shop(categoryMargins, expiryDiscount, expiryDiscountPeriodInDays);

        Integer desksCount = cliReader.readField(
            new IntegerReader("cash desks count", new LinkedHashSet<>(){{
                add(new Required());
                add(new Min<>(1));
            }})
        );

        IntStream.range(0, desksCount)
            .mapToObj(it -> new CashDesk())
            .forEach(shop.getCashDesks()::add);

        Integer cashiersCount = cliReader.readField(
            new IntegerReader("cashiers count", new LinkedHashSet<>(){{
                add(new Required());
                add(new Min<>(1));
            }})
        );

        IntStream.range(0, cashiersCount)
            .mapToObj(it -> {
                String id = cliReader.readField(
                    new StringReader(String.format("ID of cashier %d", it), new LinkedHashSet<>(){{
                        add(new Required());
                        add(new Min<>(3));
                    }})
                );

                String name = cliReader.readField(
                    new StringReader(String.format("name of cashier %d", it), new LinkedHashSet<>(){{
                        add(new Required());
                        add(new Min<>(3));
                    }})
                );

                BigDecimal monthlySalary = cliReader.readField(
                    new BigDecimalReader(
                        String.format("monthly salary of cashier %d", it),
                        new LinkedHashSet<>() {{
                            add(new Required());
                            add(new Min<>(BigDecimal.ZERO, false));
                        }}
                    )
                );

                return new Cashier(id, name, monthlySalary);
            })
            .forEach(shop::addCashier);

        app.getShops().add(shop);

        return null;
    }
}
