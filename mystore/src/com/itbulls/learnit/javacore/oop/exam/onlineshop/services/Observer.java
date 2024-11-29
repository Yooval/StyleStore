package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

/**
 * Observer interface to be implemented by any entity
 * that wants to receive notifications.
 */
public interface Observer {

    /**
     * Method to update the observer with a message.
     * 
     * @param message the notification message
     */
    void update(String message);
}
