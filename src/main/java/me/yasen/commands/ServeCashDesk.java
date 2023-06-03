package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.*;
import me.yasen.cli.CliReader;
import me.yasen.cli.ObjectFromList;
import me.yasen.exceptions.InsufficientWalletAmount;
import me.yasen.argument_matchers.DataStorage;
import me.yasen.models.CashDesk;
import me.yasen.models.Customer;
import me.yasen.models.Receipt;
import me.yasen.models.Shop;
import me.yasen.validation.Required;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

public class ServeCashDesk implements Command {
    private final App app;
    private final CliReader cliReader;
    private final PrintStream printStream;
    private final DataStorage dataStorage;

    @Inject
    public ServeCashDesk(App app, CliReader cliReader, PrintStream printStream, DataStorage dataStorage) {
        this.app = app;
        this.cliReader = cliReader;
        this.printStream = printStream;
        this.dataStorage = dataStorage;
    }

    @Override
    public Void call() {
        Shop shop = cliReader.readField(
            new ObjectFromList<>("shop", app.getShops(), new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        List<CashDesk> openedCashDesks = shop.getCashDesks().stream().filter(CashDesk::isOpened).toList();
        CashDesk cashDesk = cliReader.readField(
            new ObjectFromList<>("cash desk", openedCashDesks, new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        Customer frontCustomer = cashDesk.getQueue().poll();

        if (frontCustomer == null) {
            printStream.println("No customers waiting on the cash desk!");
            return null;
        }

        BigDecimal cartTotal = getCustomerCartTotal(shop, frontCustomer);

        // amount in cart more than amount in wallet
        if (cartTotal.compareTo(frontCustomer.getWalletAmount()) > 0) {
            frontCustomer.getProductQuantityBySku().forEach(shop::unmarkAsInCart);

            throw new InsufficientWalletAmount(cartTotal.subtract(frontCustomer.getWalletAmount()));
        }

        frontCustomer.getWalletAmount().subtract(cartTotal);
        Receipt receipt = new Receipt(shop, frontCustomer.getProductQuantityBySku(), cashDesk.getCashier(), LocalDateTime.now());

        shop.fiscalize(receipt);
//        dataStorage.serialize(receipt, String.format("%s.ser", receipt.getUuid()));

        printStream.printf("FISCAL RECEIPT: %s\n", receipt);

        return null;
    }

    protected BigDecimal getCustomerCartTotal(Shop shop, Customer customer) {
        return customer.getProductQuantityBySku()
            .entrySet()
            .stream()
            .map(it -> shop.getProductSellPrice(it.getKey()).multiply(it.getValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
