package me.yasen.exceptions;

import java.math.BigDecimal;

public class InsufficientWalletAmount extends Exception {
    public InsufficientWalletAmount(BigDecimal missingAmount) {
        super(String.format("Customer needs %s more to complete the order.", missingAmount));
    }
}
