package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

import java.util.List;

// This file represents a shopping cart where users can manage products.
public interface Cart {

    /**
     * Adds a product to the cart.
     * 
     * @param product the product to add
     */
    void addProduct(Product product);

    /**
     * Removes a product from the cart. If the product quantity in the cart is greater than one,
     * it decrements the quantity. If the quantity is one, it removes the product from the cart.
     * 
     * @param product the product to remove
     * @return true if the product was successfully removed, false if the product is not in the cart
     */
    boolean removeProduct(Product product);

    /**
     * Clears all products from the cart and restores their quantities to stock.
     */
    void clear();

    /**
     * Returns all the products in the cart as a list.
     * 
     * @return a list of products in the cart
     */
    List<Product> getProducts(); // Updated to return List<Product>

    /**
     * Calculates the total bill for the products in the cart.
     * 
     * @return the total cost of all products in the cart
     */
    double getTotalBill();

    /**
     * Checks if the cart contains the given product.
     * 
     * @param product the product to check
     * @return true if the product is in the cart, false otherwise
     */
    boolean containsProduct(Product product);

    /**
     * Gets the quantity of a specific product in the cart.
     * 
     * @param product the product whose quantity is to be retrieved
     * @return the quantity of the product in the cart
     */
    int getProductQuantity(Product product);

    /**
     * Checks if the cart is empty.
     * 
     * @return true if the cart is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Decorator for Cart to add dynamic functionality like discounts or promotions.
     */
    abstract class CartDecorator implements Cart {
        protected Cart cart;

        public CartDecorator(Cart cart) {
            this.cart = cart;
        }

        @Override
        public void addProduct(Product product) {
            cart.addProduct(product);
        }

        @Override
        public boolean removeProduct(Product product) {
            return cart.removeProduct(product);
        }

        @Override
        public void clear() {
            cart.clear();
        }

        @Override
        public List<Product> getProducts() {
            return cart.getProducts();
        }

        @Override
        public double getTotalBill() {
            return cart.getTotalBill();
        }

        @Override
        public boolean containsProduct(Product product) {
            return cart.containsProduct(product);
        }

        @Override
        public int getProductQuantity(Product product) {
            return cart.getProductQuantity(product);
        }

        @Override
        public boolean isEmpty() {
            return cart.isEmpty();
        }
    }

    /**
     * Example of a discount decorator for Cart.
     */
    class DiscountCartDecorator extends CartDecorator {
        private double discountPercentage;

        public DiscountCartDecorator(Cart cart, double discountPercentage) {
            super(cart);
            this.discountPercentage = discountPercentage;
        }

        @Override
        public double getTotalBill() {
            double originalTotal = super.getTotalBill();
            double discount = originalTotal * (discountPercentage / 100);
            return originalTotal - discount;
        }
    }
}
