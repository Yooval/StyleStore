package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultUser;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.UserManagementService;

// This file implements UserManagementService with user registration and validation.

public class DefaultUserManagementService implements UserManagementService {

    private static final String NOT_UNIQUE_EMAIL_ERROR_MESSAGE = "This email is already used by another user. Please, use another email.";
    private static final String EMPTY_EMAIL_ERROR_MESSAGE = "You have to input email to register. Please, try one more time.";
    private static final String NO_ERROR_MESSAGE = "";

    private static DefaultUserManagementService instance;

    private List<User> users;
    private Map<Integer, User> userMap = new HashMap<>();

    private DefaultUserManagementService() {
        users = new ArrayList<>();
    }

    public static UserManagementService getInstance() {
        if (instance == null) {
            instance = new DefaultUserManagementService();
        }
        return instance;
    }

    @Override
    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public String registerUser(User user) {
        if (user == null) {
            return "User cannot be null.";
        }

        String errorMessage = checkUniqueEmail(user.getEmail());
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }

        users.add(user); // Ensure the user is added to the list
        userMap.put(user.getId(), user); // Ensure it is mapped for retrieval by ID
        return NO_ERROR_MESSAGE;
    }

    /**
     * Factory Method for creating and registering users.
     * 
     * @param role       UserRole (e.g., CUSTOMER, SELLER, ADMIN).
     * @param firstName  First name of the user.
     * @param lastName   Last name of the user.
     * @param email      User email (must be unique).
     * @param password   User password.
     * @return           A success message or error message if validation fails.
     */
    public String createAndRegisterUser(UserRole role, String firstName, String lastName, String email, String password) {
        if (role == null || firstName == null || lastName == null || email == null || password == null) {
            return "Invalid input. All fields are required.";
        }

        String emailError = checkUniqueEmail(email);
        if (!emailError.isEmpty()) {
            return emailError;
        }

        // Dynamically create a user using the Factory Method
        User user;
        switch (role) {
            case SELLER:
                user = new Seller(firstName, lastName, email, password);
                break;
            case CUSTOMER:
            case ADMIN:
                user = new DefaultUser(firstName, lastName, email, password, role);
                break;
            default:
                return "Invalid user role provided.";
        }

        // Register the created user
        return registerUser(user);
    }

    private String checkUniqueEmail(String email) {
        if (email == null || email.isEmpty()) {
            return EMPTY_EMAIL_ERROR_MESSAGE;
        }
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return NOT_UNIQUE_EMAIL_ERROR_MESSAGE;
            }
        }
        return NO_ERROR_MESSAGE;
    }

    @Override
    public User[] getUsers() {
        return users.toArray(new User[0]);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        for (User user : users) {
            if (user != null && user.getEmail().equalsIgnoreCase(userEmail)) {
                return user;
            }
        }
        return null; // Return null if no user with that email is found
    }

    public void clearServiceState() {
        users.clear();
        userMap.clear(); // Ensure the map is also cleared
    }
}
