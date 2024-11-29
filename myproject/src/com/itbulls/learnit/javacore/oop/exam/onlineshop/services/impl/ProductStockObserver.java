package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;

/**
 * ProductStockObserver is responsible for sending notifications
 * when a product's stock level is low.
 */
public class ProductStockObserver implements NotificationObserver {

    private String observerName;

    /**
     * Constructor to initialize the observer.
     * 
     * @param observerName The name of the observer (e.g., Admin or Manager).
     */
    public ProductStockObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void update(String message) {
        // Simulate sending a low-stock notification
        System.out.printf("[%s] Low Stock Alert: %s%n", observerName, message);
    }

    /**
     * Retrieves the name of the observer.
     * 
     * @return The observer's name.
     */
    public String getObserverName() {
        return observerName;
    }

    /**
     * Sets a new name for the observer.
     * 
     * @param observerName The new name to set.
     */
    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}
