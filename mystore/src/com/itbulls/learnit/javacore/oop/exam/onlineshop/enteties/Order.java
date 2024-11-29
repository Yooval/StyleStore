package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

import java.time.LocalDateTime;
// This file encapsulates order details, including associated products and purchase information.
public interface Order {

    // Setters
    void setCreditCardNumber(String creditCardNumber);

    void setCustomerId(int customerId);

    void setProducts(Product[] products);

    // Getters
    Product[] getProducts(); // Ensures products in the order can be retrieved.

    int getCustomerId();

    double getTotalBill(); // Calculates and retrieves the total bill for the order.

    boolean isCreditCardNumberValid(String creditCardNumber); // Validates the credit card number.
    
    LocalDateTime getTimestamp(); // Retrieves the timestamp of the order.

    boolean isRefundEligible(); // Checks if the order is eligible for a refund.
    
    boolean isRefunded(); // Checks if the order has been refunded.

    void markAsRefunded(); // Marks the order as refunded.

    String getOrderSummary(); // Retrieves a string summary of the order details.
    
    String getCustomerEmail(); // Retrieves the customer's email.

    // Builder Pattern for flexible object creation
    class OrderBuilder {
        private int customerId;
        private Product[] products;
        private String creditCardNumber;
        private LocalDateTime timestamp = LocalDateTime.now(); // Default to current time
        private boolean refunded = false;

        // Builder methods
        public OrderBuilder withCustomerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public OrderBuilder withProducts(Product[] products) {
            this.products = products;
            return this;
        }

        public OrderBuilder withCreditCardNumber(String creditCardNumber) {
            this.creditCardNumber = creditCardNumber;
            return this;
        }

        public OrderBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Order build() {
            return new Order() {
                private int customerId = OrderBuilder.this.customerId;
                private Product[] products = OrderBuilder.this.products;
                private String creditCardNumber = OrderBuilder.this.creditCardNumber;
                private LocalDateTime timestamp = OrderBuilder.this.timestamp;
                private boolean refunded = OrderBuilder.this.refunded;

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
                    if (products != null) {
                        for (Product product : products) {
                            total += product.getPrice();
                        }
                    }
                    return total;
                }

                @Override
                public boolean isCreditCardNumberValid(String creditCardNumber) {
                    return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
                }

                @Override
                public LocalDateTime getTimestamp() {
                    return timestamp;
                }

                @Override
                public boolean isRefundEligible() {
                    return !refunded && timestamp.plusDays(30).isAfter(LocalDateTime.now());
                }

                @Override
                public boolean isRefunded() {
                    return refunded;
                }

                @Override
                public void markAsRefunded() {
                    this.refunded = true;
                }

                @Override
                public String getOrderSummary() {
                    return String.format(
                        "Customer ID: %d\nTotal Bill: $%.2f\nTimestamp: %s\nRefunded: %b",
                        customerId, getTotalBill(), timestamp, refunded
                    );
                }

                @Override
                public String getCustomerEmail() {
                    // Placeholder, implement based on your customer retrieval logic
                    return "customer@example.com";
                }
            };
        }
    }
}
