package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

/**
 * The NotificationSubject interface defines the contract for any subject 
 * in the Observer design pattern. It allows observers to register, deregister, 
 * and receive updates when the subject's state changes.
 */
public interface NotificationSubject {

    /**
     * Registers a new observer to the subject.
     * 
     * @param observer The observer to register.
     */
    void registerObserver(NotificationObserver observer);

    /**
     * Removes an existing observer from the subject.
     * 
     * @param observer The observer to remove.
     */
    void removeObserver(NotificationObserver observer);

    /**
     * Notifies all registered observers about a change in state.
     * 
     * @param message The message to send to the observers.
     */
    void notifyObservers(String message);
}
