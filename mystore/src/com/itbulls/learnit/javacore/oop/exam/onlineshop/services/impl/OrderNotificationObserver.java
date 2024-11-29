package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;

/**
 * The OrderNotificationObserver class is an implementation of the NotificationObserver 
 * interface, designed to handle notifications related to orders.
 */
public class OrderNotificationObserver implements NotificationObserver {

    private String observerName;

    /**
     * Constructor to initialize the observer with a name.
     * 
     * @param observerName The name or identifier of the observer.
     */
    public OrderNotificationObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void update(String message) {
        // Simulate processing the order notification
    }

    /**
     * Retrieves the observer's name.
     * 
     * @return The observer's name or identifier.
     */
    public String getObserverName() {
        return observerName;
    }

    /**
     * Sets a new name for the observer.
     * 
     * @param observerName The new name or identifier to set.
     */
    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}
