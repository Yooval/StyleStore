package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

/**
 * The NotificationObserver interface provides a mechanism for objects to 
 * receive notifications or updates about specific events in the system.
 */
public interface NotificationObserver {

    /**
     * Method to update the observer with a specific message.
     * 
     * @param message The message to notify the observer about.
     */
    void update(String message);
}
