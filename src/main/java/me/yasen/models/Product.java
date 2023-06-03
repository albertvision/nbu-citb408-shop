package me.yasen.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {
    private final String sku;
    private final String name;
    private final BigDecimal singleDeliveryPrice;
    private final Category category;
    private final LocalDate expiryDate;

    public Product(
            String sku,
            String name,
            BigDecimal singleDeliveryPrice,
            Category category,
            LocalDate expiryDate
    ) {
        this.sku = sku;
        this.name = name;
        this.singleDeliveryPrice = singleDeliveryPrice;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSingleDeliveryPrice() {
        return singleDeliveryPrice;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s)", sku, name, category);
    }
}
