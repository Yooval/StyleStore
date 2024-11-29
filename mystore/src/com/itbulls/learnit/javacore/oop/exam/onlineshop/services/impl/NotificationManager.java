package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * NotificationManager is a concrete implementation of NotificationSubject. It manages
 * a list of observers and notifies them when there are updates.
 */
public class NotificationManager implements NotificationSubject {

    private List<NotificationObserver> observers;

    /**
     * Constructor to initialize the list of observers.
     */
    public NotificationManager() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Retrieves the list of observers for debugging or further operations.
     * 
     * @return The list of registered observers.
     */
    public List<NotificationObserver> getObservers() {
        return observers;
    }
}
