package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationObserver;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * ConcreteNotificationSubject is an implementation of the NotificationSubject interface.
 * It manages a list of observers and notifies them of changes.
 */
public class ConcreteNotificationSubject implements NotificationSubject {

    private final List<NotificationObserver> observers;

    /**
     * Constructor to initialize the list of observers.
     */
    public ConcreteNotificationSubject() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(NotificationObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
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
}
