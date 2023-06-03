package me.yasen.models;

import me.yasen.exceptions.Exception;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Shop {
    private final ArrayList<CashDesk> cashDesks = new ArrayList<>();
    private final Set<Cashier> cashiers = new HashSet<>();
    private final HashMap<Product, BigDecimal> loadedProductQuantities = new HashMap<>();

    private final HashMap<Product, BigDecimal> productQuantitiesInCarts = new HashMap<>();
    private final HashMap<Product, BigDecimal> soldProductQuantities = new HashMap<>();
    private final BigDecimal expiryDiscount;
    private final Map<Category, BigDecimal> categoryMargins;
    private final Period expiryDiscountPeriod;

    private final Set<Receipt> receipts = new HashSet<>();

    public Shop(Map<Category, BigDecimal> categoryMargins, BigDecimal expiryDiscount, Period expiryDiscountPeriod) {
        this.expiryDiscount = expiryDiscount;
        this.categoryMargins = categoryMargins;
        this.expiryDiscountPeriod = expiryDiscountPeriod;
    }

    public void setCashierOnDesk(Cashier cashier, CashDesk desk) {
        // remove cashier from another desk if there
        cashDesks.stream().filter(it -> it.getCashier().equals(cashier)).findFirst().ifPresent(it -> it.setCashier(null));

        desk.setCashier(cashier);
    }

    public ArrayList<CashDesk> getCashDesks() {
        return cashDesks;
    }

    public Set<Cashier> getCashiers() {
        return cashiers;
    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
    }

    public void removeCashier(Cashier cashier) {
        cashiers.remove(cashier);
    }

    public void loadProduct(Product product, BigDecimal quantity) {
        // @todo balance > 0?
        loadedProductQuantities.put(product, loadedProductQuantities.getOrDefault(product, BigDecimal.ZERO).add(quantity));
    }

    public BigDecimal getProductSellPrice(Product product) {
        BigDecimal regularSellPrice = product.getSingleDeliveryPrice()
                .add(
                        product.getSingleDeliveryPrice().multiply(categoryMargins.get(product.getCategory()))
                );

        if (LocalDate.now().isAfter(product.getExpiryDate().minus(expiryDiscountPeriod))) {
            regularSellPrice = regularSellPrice.subtract(
                    regularSellPrice.multiply(expiryDiscount)
            );
        }

        return regularSellPrice;
    }

    public BigDecimal getProductSubTotal(Product product, BigDecimal quantity) {
        return getProductSellPrice(product).multiply(quantity);
    }

    public Set<Product> getProductsThatCanAddToCart() {
        return loadedProductQuantities
                .keySet()
                .stream().filter(this::canAddToCart)
                .collect(Collectors.toSet());
    }

    public boolean canAddToCart(Product product) {
        return getAvailableForCartQuantity(product).compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getAvailableForCartQuantity(Product product) {
        return loadedProductQuantities.getOrDefault(product, BigDecimal.ZERO)
                .subtract(soldProductQuantities.getOrDefault(product, BigDecimal.ZERO))
                .subtract(productQuantitiesInCarts.getOrDefault(product, BigDecimal.ZERO));
    }

    public void markAsInCart(Product product, BigDecimal quantity) {
        if (quantity.compareTo(getAvailableForCartQuantity(product)) > 0) {
           throw new Exception("Cannot add to cart more than in stock.");
        }

        productQuantitiesInCarts.put(
                product,
                productQuantitiesInCarts
                    .getOrDefault(product, BigDecimal.ZERO)
                    .add(quantity)
        );
    }

    public void unmarkAsInCart(Product product, BigDecimal quantity) {
        BigDecimal currentQuantityInCarts = productQuantitiesInCarts.getOrDefault(product, BigDecimal.ZERO);

        if (currentQuantityInCarts.compareTo(quantity) < 0) {
            throw new Exception("Cannot remove more than added.");
        }

        productQuantitiesInCarts.put(product, currentQuantityInCarts.subtract(quantity));
    }

    public void sellProduct(Product product, BigDecimal quantity) {
        try {
            unmarkAsInCart(product, quantity);
        } catch (RuntimeException e) {
            throw new Exception("Cannot add to cart more than in stock.");
        }

        soldProductQuantities.put(
                product,
                soldProductQuantities.getOrDefault(product, BigDecimal.ZERO)
                        .add(quantity)
        );
    }

    public void fiscalize(Receipt receipt) {
        receipt.getProductQuantities().forEach(this::sellProduct);
        receipts.add(receipt);
    }

}
