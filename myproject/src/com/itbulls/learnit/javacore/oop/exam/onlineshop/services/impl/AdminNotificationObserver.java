package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;

/**
 * The AdminNotificationObserver class is an implementation of the NotificationObserver 
 * interface, designed to notify the admin about specific events in the system.
 */
public class AdminNotificationObserver implements NotificationObserver {

    private String adminEmail;

    /**
     * Constructor to initialize the admin email for notifications.
     * 
     * @param adminEmail The email address of the admin to notify.
     */
    public AdminNotificationObserver(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    @Override
    public void update(String message) {
        // Simulate sending a notification to the admin
        System.out.printf("Notification sent to admin (%s): %s%n", adminEmail, message);
    }

    /**
     * Retrieves the email address of the admin.
     * 
     * @return The admin email address.
     */
    public String getAdminEmail() {
        return adminEmail;
    }

    /**
     * Sets a new email address for the admin.
     * 
     * @param adminEmail The new email address to set.
     */
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
