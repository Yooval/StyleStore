package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.DiscountStrategy;

/**
 * PercentageDiscountStrategy applies a percentage-based discount
 * to the total amount.
 */
public class PercentageDiscountStrategy implements DiscountStrategy {

    private double percentage;

    /**
     * Constructor to initialize the discount percentage.
     * 
     * @param percentage The discount percentage to apply (e.g., 10 for 10%).
     */
    public PercentageDiscountStrategy(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(double totalAmount) {
        return totalAmount * (percentage / 100);
    }

    /**
     * Retrieves the current discount percentage.
     * 
     * @return The discount percentage.
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Sets a new discount percentage.
     * 
     * @param percentage The new discount percentage to set.
     */
    public void setPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }
}
