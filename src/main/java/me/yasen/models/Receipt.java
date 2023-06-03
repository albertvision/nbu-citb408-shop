package me.yasen.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Receipt implements Serializable, Comparable<Receipt>{
    private final UUID uuid;
    private final Shop shop;
    private final Cashier cashier;
    private final LocalDateTime created;
    private final Map<Product, BigDecimal> productQuantities;

    public Receipt(Shop shop, Map<Product, BigDecimal> productQuantities, Cashier cashier, LocalDateTime created) {
        this.productQuantities = productQuantities;
        this.uuid = UUID.randomUUID();
        this.shop = shop;
        this.cashier = cashier;
        this.created = created;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<Product, BigDecimal> getProductQuantities() {
        return productQuantities;
    }

    public BigDecimal getTotal() {
        return productQuantities
            .entrySet()
            .stream()
            .map(it -> shop.getProductSellPrice(it.getKey()).multiply(it.getValue()))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        String productsInfo = productQuantities
            .entrySet()
            .stream()
            .map(it -> String.format(
                "%s\t%s\tx%s\t%s",
                it.getKey().getSku(),
                it.getKey().getName(),
                it.getValue(),
                shop.getProductSellPrice(it.getKey()).multiply(it.getValue())
            ))
            .reduce("", (old, next) -> String.format("%s\n%s", old, next));

        return String.format("ID: %s\nCashier: %s (%s)\nTime: %s\n---\n%s\n---\tTotal: %f", uuid, cashier.getName(), cashier.getId(), created, productsInfo, getTotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(uuid, receipt.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public int compareTo(Receipt o) {
        return created.compareTo(o.created);
    }
}
