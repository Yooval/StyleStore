package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.DiscountStrategy;

/**
 * FixedAmountDiscountStrategy applies a fixed discount amount
 * to the total amount.
 */
public class FixedAmountDiscountStrategy implements DiscountStrategy {

    private double fixedAmount;

    /**
     * Constructor to initialize the fixed discount amount.
     * 
     * @param fixedAmount The fixed discount amount to apply.
     */
    public FixedAmountDiscountStrategy(double fixedAmount) {
        if (fixedAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        this.fixedAmount = fixedAmount;
    }

    @Override
    public double calculateDiscount(double totalAmount) {
        // Ensure the discount doesn't exceed the total amount
        return Math.min(fixedAmount, totalAmount);
    }

    /**
     * Retrieves the current fixed discount amount.
     * 
     * @return The fixed discount amount.
     */
    public double getFixedAmount() {
        return fixedAmount;
    }

    /**
     * Sets a new fixed discount amount.
     * 
     * @param fixedAmount The new fixed discount amount to set.
     */
    public void setFixedAmount(double fixedAmount) {
        if (fixedAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        this.fixedAmount = fixedAmount;
    }
}
