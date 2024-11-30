package com.itbulls.learnit.javacore.oop.exam.onlineshop.factories;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;

// This file defines the Factory Method Pattern for creating users based on roles.

public interface UserFactory {
    User createUser(UserRole role, String firstName, String lastName, String email, String password);
}
