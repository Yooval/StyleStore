package com.itbulls.learnit.javacore.oop.exam.onlineshop.utils;

import java.util.regex.Pattern;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.ProductType;

// This file validates user inputs like names, emails, passwords, and product details.

public class InputValidator {

    private static final String NAME_REGEX = "^[A-Za-z]{2,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|org|net|co|io|edu)$";
    private static final int MIN_PASSWORD_LENGTH = 6;

    public static boolean isValidName(String name) {
        return name != null && Pattern.matches(NAME_REGEX, name);
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
    
    public static boolean isValidDescription(String description) {
        return description != null && ProductType.isValidType(description); // Check against enum
    }
    public static boolean isSellingPriceValid(double sellingPrice, double shoppingPrice) {
        return sellingPrice >= shoppingPrice;
    }
}
