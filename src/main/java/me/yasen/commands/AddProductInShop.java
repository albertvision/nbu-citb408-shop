package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.App;
import me.yasen.models.Product;
import me.yasen.models.Shop;
import me.yasen.cli.BigDecimalReader;
import me.yasen.cli.CliReader;
import me.yasen.cli.ObjectFromList;
import me.yasen.cli.ObjectFromMapReader;
import me.yasen.validation.Min;
import me.yasen.validation.Required;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

public class AddProductInShop implements Command {
    private final App app;
    private final CliReader cliReader;

    @Inject
    public AddProductInShop(App app, CliReader cliReader) {
        this.app = app;
        this.cliReader = cliReader;
    }

    @Override
    public Void call() {
        if (app.getShops().size() < 1) {
            return null;
        }

        Product product = cliReader.readField(
            new ObjectFromMapReader<>("SKU of product", app.getProducts(), new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        BigDecimal quantity = cliReader.readField(
                new BigDecimalReader("quantity to load", new LinkedHashSet<>(){{
                    add(new Required());
                    add(new Min<>(BigDecimal.ZERO, false));
                }})
        );

        Shop shop = cliReader.readField(
            new ObjectFromList<>("shop", app.getShops(), new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        shop.loadProduct(product, quantity);

        return null;
    }
}
