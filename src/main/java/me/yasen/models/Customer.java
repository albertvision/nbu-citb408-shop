package me.yasen.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private final Map<Product, BigDecimal> productQuantityBySku = new HashMap<>();
    private final BigDecimal walletAmount;

    public Customer(
            BigDecimal walletAmount
    ) {

        this.walletAmount = walletAmount;
    }

    public void addProductToCart(Product product, BigDecimal quantity) {
        productQuantityBySku.put(
                product,
                getProductSkuQuantity(product).add(quantity)
        );
    }

    public BigDecimal getProductSkuQuantity(Product product) {
        return productQuantityBySku.getOrDefault(product, BigDecimal.ZERO);
    }

    public Map<Product, BigDecimal> getProductQuantityBySku() {
        return productQuantityBySku;
    }

    public BigDecimal getTotalPieces() {
        return productQuantityBySku.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getWalletAmount() {
        return walletAmount;
    }
}
