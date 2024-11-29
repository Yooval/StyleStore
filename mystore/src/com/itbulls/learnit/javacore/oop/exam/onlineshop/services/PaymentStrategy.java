package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

/**
 * PaymentStrategy is the interface for implementing different
 * payment methods in the checkout process.
 */
public interface PaymentStrategy {

    /**
     * Processes the payment.
     * 
     * @param amount the total amount to be paid
     * @return true if the payment was successful, false otherwise
     */
    boolean pay(double amount);
}
