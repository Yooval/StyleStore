package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;
// This file is an enum class representing various product categories.
public enum ProductCategory {
    SHIRTS,
    PANTS,
    COATS,
    HATS;

    // Validate if a given category string matches any enum value
    public static boolean isValidCategory(String category) {
        try {
            ProductCategory.valueOf(category.toUpperCase()); // Convert to uppercase and check
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
