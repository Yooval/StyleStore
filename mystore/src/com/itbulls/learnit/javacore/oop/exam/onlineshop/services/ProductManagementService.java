package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;

// This file is an interface for managing product-related operations.

public interface ProductManagementService {

    // Method to get all products in the catalog
    Product[] getProducts();

    // Method to get a product by its ID
    Product getProductById(int id);

    // Method to add a new product to the catalog
    void addProduct(Product product);

    // Method to update the quantity of an existing product
    void updateProductQuantity(Product product, int newQuantity);
}
