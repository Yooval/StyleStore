package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.PaymentStrategy;

// This file implements payment processing using credit card details.

public class CreditCardPayment implements PaymentStrategy {

    private String creditCardNumber;

    public CreditCardPayment(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate credit card payment validation
        if (creditCardNumber == null || !creditCardNumber.matches("\\d{16}")) {
            System.out.println("Invalid credit card number. Payment failed.");
            return false;
        }

        // Simulate successful payment
        System.out.printf("Payment of $%.2f processed successfully using credit card ending in %s.\n",
                amount, creditCardNumber.substring(12));
        return true;
    }
}
