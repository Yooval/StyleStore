package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;

// This file is an interface for managing user-related operations like registration and retrieval.

public interface UserManagementService {

    String registerUser(User user);

    User[] getUsers();

    User getUserByEmail(String userEmail);
    
    User getUserById(int userId); // Add this method
    
    String createAndRegisterUser(UserRole role, String firstName, String lastName, String email, String password);

}
