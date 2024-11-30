package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.UserManagementService;

// This file implements the Proxy Pattern for controlled access to UserManagementService.

public class UserManagementServiceProxy implements UserManagementService {

    private final UserManagementService realService;

    public UserManagementServiceProxy(UserManagementService realService) {
        this.realService = realService;
    }

    @Override
    public String registerUser(User user) {
        System.out.println("LOG: Attempting to register user with email: " + user.getEmail());
        return realService.registerUser(user);
    }

    @Override
    public User[] getUsers() {
        System.out.println("LOG: Fetching all users.");
        return realService.getUsers();
    }

    @Override
    public User getUserByEmail(String userEmail) {
        System.out.println("LOG: Fetching user by email: " + userEmail);
        return realService.getUserByEmail(userEmail);
    }

    @Override
    public User getUserById(int userId) {
        System.out.println("LOG: Fetching user by ID: " + userId);
        return realService.getUserById(userId);
    }

    @Override
    public String createAndRegisterUser(UserRole role, String firstName, String lastName, String email, String password) {
        System.out.println("LOG: Creating and registering user with email: " + email);
        return realService.createAndRegisterUser(role, firstName, lastName, email, password);
    }
}
