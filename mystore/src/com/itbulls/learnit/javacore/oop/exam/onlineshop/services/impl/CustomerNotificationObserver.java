package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;

/**
 * The CustomerNotificationObserver class is an implementation of the NotificationObserver 
 * interface, designed to notify customers about specific events in the system.
 */
public class CustomerNotificationObserver implements NotificationObserver {

    private String customerEmail;

    /**
     * Constructor to initialize the customer's email for notifications.
     * 
     * @param customerEmail The email address of the customer to notify.
     */
    public CustomerNotificationObserver(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public void update(String message) {
        // Simulate sending a notification to the customer
        System.out.printf("Notification sent to customer (%s): %s%n", customerEmail, message);
    }

    /**
     * Retrieves the email address of the customer.
     * 
     * @return The customer's email address.
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Sets a new email address for the customer.
     * 
     * @param customerEmail The new email address to set.
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
