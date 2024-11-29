package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.ProductType;


// This file implements the Product entity with default attributes.

public class DefaultProduct extends Product {

    // Constructor matching the Product constructor
    public DefaultProduct(String productName, double buyingPrice, double sellingPrice, String description, int quantity, ProductType productType) {
        super(productName, buyingPrice, sellingPrice, description, quantity, productType); // Call the constructor without setting the id manually
    }

    @Override
    public String toString() {
        return "DefaultProduct{" +
                "id=" + getId() +
                ", productName='" + getProductName() + '\'' +
                ", buyingPrice=" + getBuyingPrice() +
                ", sellingPrice=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                ", quantity=" + getQuantity() +
                ", type=" + getType() +
                '}';
    }
}
