package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService handles the registration and notification
 * of observers (e.g., users, admins, sellers) for specific events.
 */
public class NotificationService {

    private List<Observer> observers; // List of registered observers

    public NotificationService() {
        observers = new ArrayList<>();
    }

    /**
     * Register an observer to receive notifications.
     * 
     * @param observer the observer to register
     */
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Unregister an observer to stop receiving notifications.
     * 
     * @param observer the observer to unregister
     */
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notify all registered observers with a message.
     * 
     * @param message the notification message
     */
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
