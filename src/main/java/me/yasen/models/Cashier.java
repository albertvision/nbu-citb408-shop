package me.yasen.models;

import java.math.BigDecimal;

public class Cashier {
    private final String id;
    private final String name;
    private BigDecimal monthlySalary;

    public Cashier(
            String id,
            String name,
            BigDecimal monthlySalary
    ) {
        this.id = id;
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", id, name);
    }
}
