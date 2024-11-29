package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

/**
 * Enum representing product types with enhanced functionality.
 */

// This file is an enum class representing specific product types.
public enum ProductType {
    PANTS, HAT, COAT, SHIRT, SHOES, COMPOSITE; // Added COMPOSITE type for composite products

    /**
     * Validates if the provided type is a valid ProductType.
     * 
     * @param type The product type as a string.
     * @return true if the type is valid, false otherwise.
     */
    public static boolean isValidType(String type) {
        try {
            ProductType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if the provided ProductType is a composite type.
     * 
     * @param type The ProductType to check.
     * @return true if the type is COMPOSITE, false otherwise.
     */
    public static boolean isCompositeType(ProductType type) {
        return type == COMPOSITE;
    }
}
