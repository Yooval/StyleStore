package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Cart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.ProductManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultProductManagementService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// This file provides a user interface for browsing and managing the product catalog.

public class ProductCatalogMenu implements Menu {

    private ApplicationContext context;
    private ProductManagementService productManagementService;

    {
        context = ApplicationContext.getInstance();
        productManagementService = DefaultProductManagementService.getInstance();
    }

    @Override
    public void start() {
        Menu menuToNavigate = null;

        Product[] products = productManagementService.getProducts();

        if (products == null || products.length == 0) {
            System.out.println("The product catalog is currently empty. Returning to the main menu.");
            new MainMenu().start();
            return;
        }

        while (true) {
            printMenuHeader();
            printProductsToConsole();

            System.out.println(
                    "Enter the product ID to add it to your cart, 'cart' to view your cart, 'checkout' to proceed, 'remove' to remove a product, 'clear cart' to clear the cart, or 'menu' to return:");
            String userInput = readUserInput();

            if (context.getLoggedInUser() == null) {
                menuToNavigate = new MainMenu();
                System.out.println("You are not logged in. Please, sign in or create a new account.");
                break;
            }

            if (userInput.equalsIgnoreCase("menu")) {
                menuToNavigate = new MainMenu();
                break;
            }

            if (userInput.equalsIgnoreCase("cart")) {
                viewCart();
                continue;
            }

            if (userInput.equalsIgnoreCase("checkout")) {
                checkoutCart();
                break;
            }

            if (userInput.equalsIgnoreCase("remove")) {
                removeProductFromCart();
                continue;
            }

            if (userInput.equalsIgnoreCase("clear cart")) {
                clearCart();
                continue;
            }

            try {
                int productId = Integer.parseInt(userInput); // Check if input is a product ID
                Product productToAddToCart = productManagementService.getProductById(productId);
                if (productToAddToCart != null) {
                    processAddToCart(productToAddToCart); // Call processAddToCart with the product
                } else {
                    System.out.println("Invalid product ID. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid product ID or command.");
            }
        }

        if (menuToNavigate != null) {
            menuToNavigate.start();
        }
    }

    private String readUserInput() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private void printProductsToConsole() {
        Product[] products = productManagementService.getProducts();
        if (products == null || products.length == 0) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("***** PRODUCT CATALOG *****");
        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            System.out.printf("%d. Product: %s | Price: $%.2f | Description: %s%n",
                    i + 1, // Start from 1 instead of 0
                    product.getProductName(),
                    product.getPrice(),
                    product.getDescription());
        }

        // Adding a line to prevent additional user input after displaying products.
    }

    private void processAddToCart(Product product) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Prompt for quantity
            System.out.printf("Enter quantity to buy (Available: %d) or type 'back' to go back: ",
                    product.getQuantity());
            String userInput = sc.nextLine().trim();

            // Check if user wants to go back
            if (userInput.equalsIgnoreCase("back")) {
                System.out.println("Returning to the product catalog...");
                return; // Exit the method to return to the previous menu
            }

            try {
                int quantityToBuy = Integer.parseInt(userInput);
                if (quantityToBuy > 0 && quantityToBuy <= product.getQuantity()) {
                    // Reduce stock quantity
                    product.setQuantity(product.getQuantity() - quantityToBuy);

                    // Add specified quantity to the cart
                    for (int i = 0; i < quantityToBuy; i++) {
                        context.getSessionCart().addProduct(product);
                    }

                    System.out.printf("Added %d of '%s' to your cart.%n", quantityToBuy, product.getProductName());
                    return; // Exit after adding to the cart
                } else {
                    System.out.printf("Invalid quantity. You can only purchase up to %d items.%n",
                            product.getQuantity());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer or type 'back' to go back.");
            }
        }
    }

    private void checkoutCart() {
        Cart sessionCart = context.getSessionCart();
        if (sessionCart == null || sessionCart.isEmpty()) {
            System.out.println("Your cart is empty. Please add products before checkout.");
            return;
        }

        double totalCost = sessionCart.getTotalBill();
        System.out.printf("Your total is $%.2f. Proceeding with checkout...\n", totalCost);

        // Navigate to CheckoutMenu for payment handling
        new CheckoutMenu().start();

        sessionCart.clear();
    }

    private void removeProductFromCart() {
        Cart sessionCart = context.getSessionCart();

        if (sessionCart == null || sessionCart.isEmpty()) {
            System.out.println("Your cart is already empty. Nothing to remove.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the product ID to remove from the cart or type 'back' to go back: ");
            String userInput = sc.nextLine().trim();

            // Check if user wants to go back
            if (userInput.equalsIgnoreCase("back")) {
                System.out.println("Returning to the product catalog...");
                return; // Exit the method to return to the previous menu
            }

            try {
                int productId = Integer.parseInt(userInput);
                Product product = productManagementService.getProductById(productId);

                if (product == null || !sessionCart.containsProduct(product)) {
                    System.out.println("Product not found in the cart. Please try again.");
                } else {
                    // Prompt for quantity to remove
                    while (true) {
                        System.out.printf("Enter quantity to remove (In Cart: %d) or type 'back' to cancel: ",
                                sessionCart.getProductQuantity(product));
                        String quantityInput = sc.nextLine().trim();

                        // Check if user wants to go back
                        if (quantityInput.equalsIgnoreCase("back")) {
                            System.out.println("Returning to product removal menu...");
                            break;
                        }

                        try {
                            int quantityToRemove = Integer.parseInt(quantityInput);
                            int cartQuantity = sessionCart.getProductQuantity(product);

                            if (quantityToRemove > 0 && quantityToRemove <= cartQuantity) {
                                // Update cart and product stock
                                for (int i = 0; i < quantityToRemove; i++) {
                                    sessionCart.removeProduct(product);
                                }
                                product.setQuantity(product.getQuantity() + quantityToRemove);

                                System.out.printf("Removed %d of '%s' from your cart.%n", quantityToRemove,
                                        product.getProductName());
                                return; // Exit after removing from the cart
                            } else {
                                System.out.printf("Invalid quantity. You can only remove up to %d items.%n",
                                        cartQuantity);
                            }
                        } catch (NumberFormatException e) {
                            System.out
                                    .println("Invalid input. Please enter a valid integer or type 'back' to go back.");
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid product ID or type 'back' to go back.");
            }
        }
    }

    private void clearCart() {
        Cart sessionCart = context.getSessionCart();

        if (sessionCart == null || sessionCart.isEmpty()) {
            System.out.println("Your cart is already empty. Nothing to clear.");
            return;
        }

        sessionCart.clear();
        System.out.println("Your cart has been cleared.");
    }

    private void viewCart() {
        Cart sessionCart = context.getSessionCart();

        if (sessionCart == null || sessionCart.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Your cart contains:");

            // Map to keep track of product quantities and price
            Map<String, Integer> productQuantities = new HashMap<>();
            double totalPrice = 0.0;

            // Iterate through the products in the cart and track quantities
            for (Product product : sessionCart.getProducts()) {
                String productName = product.getProductName();
                double productPrice = product.getPrice();
                productQuantities.put(productName, productQuantities.getOrDefault(productName, 0) + 1);
                totalPrice += productPrice; // Add to total price
            }

            // Display the cart contents with quantities, product name, and price
            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();

                // Find the price of the product (assuming all products with the same name have
                // the same price)
                Product sampleProduct = null;
                for (Product product : sessionCart.getProducts()) {
                    if (product.getProductName().equals(productName)) {
                        sampleProduct = product;
                        break;
                    }
                }

                if (sampleProduct != null) {
                    double pricePerItem = sampleProduct.getPrice();
                    double totalItemPrice = pricePerItem * quantity; // Total price for this product

                    System.out.printf("%d x %s - $%.2f each (Total: $%.2f)%n", quantity, productName, pricePerItem,
                            totalItemPrice);
                }
            }

            // Display the total price of all products in the cart
            System.out.printf("Total: $%.2f%n", totalPrice);
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** PRODUCT CATALOG *****");
    }
}
