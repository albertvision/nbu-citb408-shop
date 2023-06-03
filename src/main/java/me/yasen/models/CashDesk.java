package me.yasen.models;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

public class CashDesk {
    private Cashier cashier = null;
    private final Queue<Customer> queue = new LinkedList<>();

    public CashDesk() {
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Queue<Customer> getQueue() {
        return queue;
    }

    public BigDecimal getTotalPieces() {
        return queue.stream()
                .map(Customer::getTotalPieces)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public boolean isClosed() {
        return !isOpened();
    }

    public boolean isOpened() {
        return cashier != null;
    }

    @Override
    public String toString() {
        return String.format("%d people with %f pieces in total", queue.size(), getTotalPieces());
    }
}
