package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

import java.util.ArrayList;
import java.util.List;
// This file defines attributes for products, such as name, price, and description.
/**
 * The Product class represents a single product or a composite product in the shop.
 * Implements the Composite Pattern for flexible product grouping.
 */
public class Product {
    private int id;
    private static int idCounter = 1; // Static counter for unique IDs
    private String productName;
    private double buyingPrice; // New buying price field
    private double price;       // Selling price
    private String description;
    private int quantity;
    private ProductType type;   // Product type field

    // Composite Pattern fields
    private List<Product> subProducts; // List of sub-products for composite products

    // Constructor for individual products
    public Product(String productName, double buyingPrice, double sellingPrice, String description, int quantity, ProductType type) {
        this.id = idCounter++;
        this.productName = productName;
        this.buyingPrice = buyingPrice;
        this.price = sellingPrice;
        this.description = description;
        this.quantity = quantity;
        this.type = type;
        this.subProducts = new ArrayList<>(); // Initialize sub-products list
    }

    // Constructor with explicit ID for use in DefaultProduct
    public Product(int id, String productName, double buyingPrice, double sellingPrice, String description, int quantity, ProductType type) {
        this.id = id;
        this.productName = productName;
        this.buyingPrice = buyingPrice;
        this.price = sellingPrice;
        this.description = description;
        this.quantity = quantity;
        this.type = type;
        this.subProducts = new ArrayList<>(); // Initialize sub-products list
    }

    // Constructor for composite products
    public Product(String productName, String description, ProductType type) {
        this.id = idCounter++;
        this.productName = productName;
        this.description = description;
        this.type = type;
        this.subProducts = new ArrayList<>(); // Initialize sub-products list
    }

    // Composite methods

    /**
     * Adds a sub-product to this composite product.
     * 
     * @param product The sub-product to add.
     */
    public void addSubProduct(Product product) {
        subProducts.add(product);
    }

    /**
     * Removes a sub-product from this composite product.
     * 
     * @param product The sub-product to remove.
     */
    public void removeSubProduct(Product product) {
        subProducts.remove(product);
    }

    /**
     * Gets the list of sub-products for this composite product.
     * 
     * @return List of sub-products.
     */
    public List<Product> getSubProducts() {
        return subProducts;
    }

    /**
     * Calculates the total price of this product, including all sub-products.
     * 
     * @return Total price.
     */
    public double calculateTotalPrice() {
        double totalPrice = this.price;
        for (Product subProduct : subProducts) {
            totalPrice += subProduct.calculateTotalPrice();
        }
        return totalPrice;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    // String representation of the product
    @Override
    public String toString() {
        return String.format(
                "Product{id=%d, name='%s', price=%.2f, description='%s', quantity=%d, type=%s, subProducts=%d}",
                id, productName, price, description, quantity, type, subProducts.size()
        );
    }
}
