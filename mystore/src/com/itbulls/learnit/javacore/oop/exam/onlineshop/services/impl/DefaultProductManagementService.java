package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.ProductManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

//  This file implements ProductManagementService for managing the product catalog.

public class DefaultProductManagementService implements ProductManagementService {

    private List<Product> products;  // List to store products
    private static DefaultProductManagementService instance;
    private ApplicationContext context; // ApplicationContext for accessing budget and email services

    private DefaultProductManagementService() {
        products = new ArrayList<>();  // Initialize the product list
        context = ApplicationContext.getInstance(); // Initialize context
    }

    // Singleton pattern to get the instance of the service
    public static DefaultProductManagementService getInstance() {
        if (instance == null) {
            instance = new DefaultProductManagementService();
        }
        return instance;
    }

    @Override
    public Product[] getProducts() {
        return products.toArray(new Product[0]);
    }

    @Override
    public Product getProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);  // Add product to the catalog
    }

    /**
     * Updates the quantity of an existing product and sends an admin notification email.
     *
     * @param product The product to update.
     * @param newQuantity The new total quantity.
     */
    public void updateProductQuantity(Product product, int newQuantity) {
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        // Calculate cost of additional quantity
        int additionalQuantity = newQuantity - product.getQuantity();
        double cost = additionalQuantity * product.getBuyingPrice();

        // Check if the store has enough budget
        if (cost > context.getStoreBudget()) {
            System.out.println("Insufficient budget to update the quantity.");
            return;
        }

        // Update product quantity
        product.setQuantity(newQuantity);

        // Deduct from the store budget
        context.updateStoreBudget(-cost);

        // Notify admin
       // sendQuantityUpdateEmail(product, newQuantity, cost);
    }

    /**
     * Sends an email to the admin with details about the product quantity update.
     *
     * @param product The updated product.
     * @param updatedQuantity The new total quantity.
     * @param cost The cost of the quantity update.
     */


	
}
