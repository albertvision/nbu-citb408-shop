package me.yasen.commands;

import me.yasen.App;
import me.yasen.cli.CliReader;
import me.yasen.models.Category;
import me.yasen.models.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static me.yasen.argument_matchers.CliReaderMatcher.whereName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AddProductTest {

    @Test
    void call() {
        App app = mock(App.class);

        // pass test products map to App, so we can use it later
        Map<String, Product> products = new HashMap<>();
        when(app.getProducts()).thenReturn(products);

        CliReader cliReader = mock(CliReader.class);

        when(cliReader.readField(whereName("product SKU")))
            .thenReturn("SKU-TEST-1");

        when(cliReader.readField(whereName("product name")))
            .thenReturn("product sample name");

        when(cliReader.readField(whereName("single delivery price")))
            .thenReturn(BigDecimal.valueOf(100.5));

        when(cliReader.readField(whereName("category of product")))
            .thenReturn(Category.GROCERY);

        LocalDate expiryDate = LocalDate.now();

        when(cliReader.readField(whereName("expiry date")))
            .thenReturn(expiryDate);

        AddProduct addProduct = new AddProduct(app, cliReader);
        addProduct.call();

        assertTrue(products.containsKey("SKU-TEST-1"));

        Product product = products.get("SKU-TEST-1");

        assertEquals("SKU-TEST-1", product.getSku());
        assertEquals("product sample name", product.getName());
        assertEquals(BigDecimal.valueOf(100.5), product.getSingleDeliveryPrice());
        assertEquals(Category.GROCERY, product.getCategory());
        assertEquals(expiryDate, product.getExpiryDate());
    }
}