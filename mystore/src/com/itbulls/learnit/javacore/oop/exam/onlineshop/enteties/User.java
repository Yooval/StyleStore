package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

/**
 * The User interface represents the blueprint for user-related entities
 * in the online shop system. It defines the required methods that 
 * any implementing class must provide.
 */

// This file encapsulates user-related data, including personal details and roles.

public interface User {

    // Getters for user details
    String getFirstName();    // Retrieve the user's first name
    String getLastName();     // Retrieve the user's last name
    String getPassword();     // Retrieve the user's password
    String getEmail();        // Retrieve the user's email
    int getId();              // Retrieve the user's unique ID
    UserRole getRole();       // Retrieve the user's role (e.g., CUSTOMER, ADMIN)

    // Setters for updating user details
    void setPassword(String newPassword); // Update the user's password
    void setEmail(String newEmail);       // Update the user's email
}
