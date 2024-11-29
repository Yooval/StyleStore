package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

/**
 * The DiscountStrategy interface defines the blueprint for applying
 * discounts to purchases. Implementations of this interface will provide
 * specific discount calculation logic.
 */
public interface DiscountStrategy {

    /**
     * Calculates the discount for a given total amount.
     *
     * @param totalAmount The original total amount before applying the discount.
     * @return The discount amount to be applied.
     */
    double calculateDiscount(double totalAmount);
}
