package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;

// This file extends the User class to include seller-specific attributes, such as credit points.

public class Seller implements User {

    private static int userCounter = 0;

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private double creditPoints; // Additional field for seller-specific data

    // Static block to increment unique IDs
    {
        id = ++userCounter;
    }

    // Constructor
    public Seller(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserRole.SELLER; // Default role for a seller
        this.creditPoints = 0.0;    // Start with zero credit points
    }

    // Implementing methods from User interface
    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    @Override
    public void setPassword(String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            this.password = newPassword;
        }
    }

    @Override
    public void setEmail(String newEmail) {
        if (newEmail != null && !newEmail.isEmpty()) {
            this.email = newEmail;
        }
    }

    // Additional seller-specific methods
    public void helpCustomer(double billAmount) {
        if (billAmount > 0) {
            double pointsEarned = billAmount * 0.1; // Seller earns 10% of the bill in points
            creditPoints += pointsEarned;
            System.out.println(firstName + " earned " + pointsEarned + " points. Total: " + creditPoints);
        } else {
            System.out.println("Invalid bill amount. Points not updated.");
        }
    }

    public double getCreditPoints() {
        return creditPoints;
    }

    public void usePoints(double amount) {
        if (amount > creditPoints) {
            throw new IllegalArgumentException("Insufficient credit points to use.");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Amount to use must be greater than zero.");
        }
        creditPoints -= amount;
        System.out.println("Used " + amount + " points. Remaining points: " + creditPoints);
    }

    // String representation
    @Override
    public String toString() {
        return String.format("First Name: %s\t\tLast Name: %s\t\tRole: SELLER\t\tPoints: %.2f",
                firstName, lastName, creditPoints);
    }

    public void setCreditPoints(double creditPoints) {
        if (creditPoints < 0) {
            throw new IllegalArgumentException("Credit points cannot be negative");
        }
        this.creditPoints = creditPoints;
    }
}
