package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;

//  This file extends the User class to include admin-specific functionality.

public class Admin implements User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int id;
    private UserRole role;

    public Admin(String firstName, String lastName, String email, String password, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.role = UserRole.ADMIN;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
        this.password = newPassword;
    }

    @Override
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }
}
