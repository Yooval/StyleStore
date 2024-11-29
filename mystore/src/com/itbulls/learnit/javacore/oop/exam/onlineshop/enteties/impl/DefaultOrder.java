package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultUserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//This file implements the Order interface with default order functionalities.

public class DefaultOrder implements Order {

    private int customerId;
    private Product[] products;
    private String creditCardNumber;
    private double totalBill;
    private LocalDateTime timestamp;
    private boolean refunded; // Flag to track if the order has been refunded

    public DefaultOrder() {
        this.timestamp = LocalDateTime.now(); // Set order creation time
        this.refunded = false; // Default order is not refunded
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isRefundEligible() {
        if (refunded) {
            return false; // If the order has been refunded, it cannot be refunded again
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);
        return duration.toMinutes() < 10; // Refund allowed only within 10 minutes
    }

    @Override
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void setProducts(Product[] products) {
        this.products = products;
    }

    @Override
    public Product[] getProducts() {
        return products;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public double getTotalBill() {
        double total = 0.0;
        for (Product product : products) {
            if (product.getSubProducts().isEmpty()) {
                total += product.getPrice(); // Individual product price
            } else {
                total += product.calculateTotalPrice(); // Composite product price
            }
        }
        return total;
    }

    @Override
    public boolean isCreditCardNumberValid(String creditCardNumber) {
        return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
    }

    @Override
    public String getOrderSummary() {
        StringBuilder summary = new StringBuilder();
        var loggedInUser = ApplicationContext.getInstance().getLoggedInUser();
        String customerInfo = String.format("%s %s (%s)", loggedInUser.getFirstName(), loggedInUser.getLastName(), loggedInUser.getEmail());

        summary.append("Order Summary:\n")
               .append("Customer: ").append(customerInfo).append("\n");

        Map<String, Integer> productQuantities = new HashMap<>();
        double totalBill = 0.0;

        for (Product product : products) {
            String productName = product.getProductName();
            productQuantities.put(
                productName,
                productQuantities.getOrDefault(productName, 0) + 1
            );
            totalBill += product.getPrice();
        }

        summary.append("Products:\n");
        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            summary.append(String.format(" - %d x %s\n", entry.getValue(), entry.getKey()));
        }

        summary.append(String.format("Total Bill: $%.2f\n", totalBill));
        return summary.toString();
    }

    @Override
    public String getCustomerEmail() {
        if (customerId <= 0) {
            return "Unknown";
        }
        User customer = DefaultUserManagementService.getInstance().getUserById(customerId);
        return customer != null ? customer.getEmail() : "Unknown";
    }

    @Override
    public String toString() {
        return getOrderSummary(); // Ensure toString() uses the formatted order summary
    }

    @Override
    public boolean isRefunded() {
        return refunded; // Return the refund status
    }

    @Override
    public void markAsRefunded() {
        this.refunded = true; // Mark the order as refunded
        System.out.println("Order marked as refunded.");
    }
}
