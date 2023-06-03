package me.yasen.commands;

import jakarta.inject.Inject;
import me.yasen.App;
import me.yasen.models.Category;
import me.yasen.models.Product;
import me.yasen.cli.*;
import me.yasen.validation.Min;
import me.yasen.validation.Required;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Sample input:
 * <pre>
 *     SKU-1234
 *     My product
 *     10.55
 *     GROCERY
 *     2023-05-23
 * </pre>
 */
public class AddProduct implements Command {

    private final App app;
    private final CliReader cliReader;

    @Inject
    public AddProduct(App app, CliReader cliReader) {
        this.app = app;
        this.cliReader = cliReader;
    }

    @Override
    public Void call() {
        String sku = cliReader.readField(
                new StringReader("product SKU", new LinkedHashSet<>() {{
                    add(new Required());
                    add(new Min<>(3));
                }})
        );

        String name = cliReader.readField(
                new StringReader("product name", new LinkedHashSet<>(){{
                    add(new Required());
                }})
        );

        BigDecimal singleDeliveryPrice = cliReader.readField(
                new BigDecimalReader("single delivery price", new LinkedHashSet<>(){{
                    add(new Required());
                    add(new Min<>(BigDecimal.ZERO, false));
                }})
        );

        Category category = cliReader.readField(
            new EnumReader<>("category of product", Category.class, new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        LocalDate expiryDate = cliReader.readField(
            new LocalDateReader("expiry date", new LinkedHashSet<>(){{
                add(new Required());
            }})
        );

        app.getProducts().put(sku, new Product(sku, name, singleDeliveryPrice, category, expiryDate));

        return null;
    }
}
