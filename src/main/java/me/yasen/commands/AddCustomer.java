package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.*;
import me.yasen.cli.BigDecimalReader;
import me.yasen.cli.CliReader;
import me.yasen.cli.ObjectFromList;
import me.yasen.cli.ObjectFromMapReader;
import me.yasen.models.CashDesk;
import me.yasen.models.Customer;
import me.yasen.models.Product;
import me.yasen.models.Shop;
import me.yasen.validation.Max;
import me.yasen.validation.Min;
import me.yasen.validation.Required;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddCustomer implements Command {
    private final App app;
    private final CliReader cliReader;
    private final PrintStream printStream;

    @Inject
    public AddCustomer(App app, CliReader cliReader, PrintStream printStream) {
        this.app = app;
        this.cliReader = cliReader;
        this.printStream = printStream;
    }

    @Override
    public Void call() {
        BigDecimal walletAmount = cliReader.readField(
                new BigDecimalReader("wallet amount", new LinkedHashSet<>() {{
                    add(new Required());
                    add(new Min<>(BigDecimal.ZERO));
                }})
        );

        Customer customer = new Customer(walletAmount);

        Shop shop = cliReader.readField(
            new ObjectFromList<>("shop", app.getShops(), new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        while (true) {
            Set<Product> purchasableProducts = shop.getProductsThatCanAddToCart();

            Product product = cliReader.readField(
                new ObjectFromMapReader<>(
                    "product to buy",
                    purchasableProducts.stream()
                        .collect(Collectors.toMap(Product::getSku, Function.identity())),
                    new LinkedHashSet<>()
                )
            );

            if (product == null) {
                break;
            }

            BigDecimal availableQuantity = shop.getAvailableForCartQuantity(product);
            BigDecimal quantityToBuy = cliReader.readField(
                new BigDecimalReader(
                    "quantity to buy",
                    new LinkedHashSet<>(){{
                        add(new Required());
                        add(new Min<>(BigDecimal.ZERO, false));
                        add(new Max<>(availableQuantity));
                    }}
                )
            );

            customer.addProductToCart(product, quantityToBuy);
            shop.markAsInCart(product, quantityToBuy);
        }

        if (customer.getTotalPieces().compareTo(BigDecimal.ZERO) == 0) {
            printStream.println("Your cart is empty. Goodbye!");
            return null;
        }

        List<CashDesk> openedCashDesks = shop.getCashDesks()
            .stream()
            .filter(CashDesk::isOpened)
            .toList();

        CashDesk cashDesk = cliReader.readField(
            new ObjectFromList<>("cash desk", openedCashDesks, new LinkedHashSet<>() {{
                add(new Required());
            }})
        );

        cashDesk.getQueue().add(customer);

        return null;
    }

    protected void deleteCustomer(Shop shop, Customer customer) {
        customer.getProductQuantityBySku()
                .forEach(shop::unmarkAsInCart);

        shop.getCashDesks().forEach(it -> it.getQueue().removeIf(itCustomer -> itCustomer.equals(customer)));
    }
}
