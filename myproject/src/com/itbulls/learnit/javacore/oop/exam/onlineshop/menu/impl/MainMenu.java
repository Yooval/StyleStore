package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.ProductType;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultProduct;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.OrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.ProductManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultOrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultProductManagementService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// This file acts as the central navigation hub for the application.

public class MainMenu implements Menu {

    private ApplicationContext context;
    private ProductManagementService productManagementService;
    private OrderManagementService orderManagementService;

    {
        context = ApplicationContext.getInstance();
        productManagementService = DefaultProductManagementService.getInstance();
        orderManagementService = DefaultOrderManagementService.getInstance();
    }

    @Override
    public void start() {
        Menu menuToNavigate = null;

        while (true) {
            printMenuHeader();
            String userInput = readUserInput();

            switch (userInput) {
                case "1":
                    menuToNavigate = new SignUpMenu();
                    break;
                case "2":
                    menuToNavigate = new SignInMenu();
                    break;
                case "3":
                    menuToNavigate = new SignOutMenu();
                    break;
                case "4":
                    menuToNavigate = new ProductCatalogMenu();
                    break;
                case "5":
                    menuToNavigate = new MyOrdersMenu();
                    break;
                case "6":
                    menuToNavigate = new SettingsMenu();
                    break;
                case "7":
                    menuToNavigate = new CustomerListMenu();
                    break;
                case "8":
                    if (context.getLoggedInUser() == null
                            || !context.getLoggedInUser().getRole().equals(UserRole.ADMIN)) {
                        System.out.println("Access denied. Only admins can view purchase history.");
                    } else {
                        handleManageStore();
                    }
                    continue;
                case "9":
                    if (context.getLoggedInUser() == null
                            || !context.getLoggedInUser().getRole().equals(UserRole.ADMIN)) {
                        System.out.println("Access denied. Only admins can view purchase history.");
                    } else {
                        viewPurchaseHistory();
                    }
                    continue;
                default:
                    System.out.println("Invalid option. Please try again.");
                    continue;
            }

            if (menuToNavigate != null) {
                menuToNavigate.start();
            }
        }
    }

    private String readUserInput() {
        System.out.print("Please enter an option: ");
        return new Scanner(System.in).nextLine();
    }

    private void handleManageStore() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("***** MANAGE STORE *****");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Price");
            System.out.println("3. Update Product Description");
            System.out.println("4. Return Product");
            System.out.println("5. Return to Main Menu");
            System.out.print("Please choose an option: ");
            String userInput = sc.nextLine();

            switch (userInput) {
                case "1":
                    handleAddProduct();
                    break;
                case "2":
                    updateProductPrice();
                    break;
                case "3":
                    updateProductDescription();
                    break;
                case "4":
                    handleReturnProduct();
                    break;
                case "5":
                    return; // Exit the submenu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void handleReturnProduct() {
        // Ensure the user is a manager (for example, role-based check)
        if (!context.getLoggedInUser().getRole().equals(UserRole.ADMIN)) {
            System.out.println("You must be an admin to return items.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("***** RETURN PRODUCT *****");
        System.out.println("Select a product to return:");

        // List all products (retrieving them from product management service)
        Product[] products = productManagementService.getProducts();
        if (products == null || products.length == 0) { // Check if there are no products
            System.out.println("No products available to return.");
            return;
        }

        // Display products for return selection
        for (int i = 0; i < products.length; i++) { // Using products.length for array length
            Product product = products[i];
            System.out.printf("%d. %s - $%.2f (Available: %d)\n", i + 1, product.getProductName(), product.getPrice(), product.getQuantity());
        }

        System.out.print("Enter the product number to return (or 0 to cancel): ");
        int productIndex = sc.nextInt() - 1;  // Index of selected product (adjusted for zero-indexing)
        if (productIndex < 0 || productIndex >= products.length) { // Use products.length to ensure valid selection
            System.out.println("Invalid product selection. Returning to the previous menu.");
            return;
        }

        Product productToReturn = products[productIndex];

        // Ask how many items they want to return (all or part)
        System.out.print("Enter the quantity to return (1 to " + productToReturn.getQuantity() + "): ");
        int quantityToReturn = sc.nextInt();

        // Ensure valid quantity input
        if (quantityToReturn <= 0 || quantityToReturn > productToReturn.getQuantity()) {
            System.out.println("Invalid quantity. Returning to the previous menu.");
            return;
        }

        // Calculate the refund value (80% of the product's value)
        double refundValue = productToReturn.getPrice() * 0.80 * quantityToReturn;

        // Update store budget
        context.updateStoreBudget(refundValue);

        // Update the product quantity
        productToReturn.setQuantity(productToReturn.getQuantity() - quantityToReturn);

        // Provide feedback
        System.out.printf("You have successfully returned %d of '%s'. $%.2f has been added to the store budget.\n",
                quantityToReturn, productToReturn.getProductName(), refundValue);
    }



    void handleAddProduct() {
        Scanner sc = new Scanner(System.in);

        // Validate Product Name
        String productName = validateProductName(sc);

        // Check if the product already exists
        Product existingProduct = findExistingProduct(productName);

        if (existingProduct != null) {
            handleExistingProduct(existingProduct, sc);
            return; // Exit after updating the quantity
        }

        // If new product, proceed with validations
        double shoppingPrice = validateShoppingPrice(sc);
        double sellingPrice = validateSellingPrice(sc, shoppingPrice);
        int quantity = validateQuantity(sc, shoppingPrice);
        ProductType productType = validateProductType(sc);
        String description = validateDescription(sc);

        // Create and add the new product
        Product newProduct = new DefaultProduct(productName, shoppingPrice, sellingPrice, description, quantity,
                productType);
        productManagementService.addProduct(newProduct);

        // Deduct the total cost from the store budget
        double totalCost = shoppingPrice * quantity;
        context.updateStoreBudget(-totalCost);

        // Notify and confirm the addition
        context.sendBudgetUpdateEmail(-totalCost, context.getStoreBudget(), newProduct);
        System.out.println("Product added successfully!");
    }

    private String validateProductName(Scanner sc) {
        while (true) {
            System.out.print("Enter product name (letters and spaces only): ");
            String productName = sc.nextLine().trim();
            if (!productName.isEmpty() && productName.matches("^[A-Za-z ]+$")) {
                return productName;
            }
            System.out.println("Invalid product name. Please enter only letters and spaces.");
        }
    }

    private Product findExistingProduct(String productName) {
        for (Product product : productManagementService.getProducts()) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    private void handleExistingProduct(Product existingProduct, Scanner sc) {
        System.out.printf("Product '%s' already exists. Current quantity: %d, Price: $%.2f%n",
                existingProduct.getProductName(), existingProduct.getQuantity(), existingProduct.getBuyingPrice());

        double budget = context.getStoreBudget();
        int maxQuantity = (int) (budget / existingProduct.getBuyingPrice());
        System.out.printf("You can afford to add up to %d more units with your current budget of $%.2f.%n",
                maxQuantity, budget);

        int additionalQuantity = validateAdditionalQuantity(sc, existingProduct, maxQuantity);

        // Update quantity using ProductManagementService
        int newQuantity = existingProduct.getQuantity() + additionalQuantity;
        DefaultProductManagementService.getInstance().updateProductQuantity(existingProduct, newQuantity);
        // Calculate the total cost for the additional quantity
        double additionalCost = additionalQuantity * existingProduct.getBuyingPrice();

        // Deduct the cost from the store budget
        context.updateStoreBudget(-additionalCost);

        // Send an email notification about the budget update
        context.sendBudgetUpdateEmail(-additionalCost, context.getStoreBudget(), existingProduct);

        System.out.println("Product quantity updated successfully!");
    }

    private double validateShoppingPrice(Scanner sc) {
        double budget = context.getStoreBudget();
        while (true) {
            System.out.printf(
                    "Enter shopping price (positive number, up to 100,000, and not greater than store budget $%.2f): ",
                    budget);
            if (sc.hasNextDouble()) {
                double price = sc.nextDouble();
                if (price > 0 && price <= 100000 && price <= budget) {
                    return price;
                }
                System.out.println("Invalid price. Ensure it is within budget and limits.");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // Clear invalid input
            }
        }
    }

    private double validateSellingPrice(Scanner sc, double shoppingPrice) {
        while (true) {
            System.out.print("Enter selling price (must be >= shopping price and <= 100,000): ");
            if (sc.hasNextDouble()) {
                double price = sc.nextDouble();
                if (price >= shoppingPrice && price <= 100000) {
                    return price;
                }
                System.out.println("Invalid selling price. Must be >= shopping price and <= 100,000.");
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.next(); // Clear invalid input
            }
        }
    }

    private int validateQuantity(Scanner sc, double shoppingPrice) {
        double budget = context.getStoreBudget();
        while (true) {
            System.out.printf("Enter quantity (positive integer):");
            if (sc.hasNextInt()) {
                int quantity = sc.nextInt();
                double totalCost = shoppingPrice * quantity;
                if (quantity > 0 && totalCost <= budget) {
                    return quantity;
                }
                System.out.printf("Invalid quantity. You can purchase up to %d units.%n",
                        (int) (budget / shoppingPrice));
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); // Clear invalid input
            }
        }
    }

    private ProductType validateProductType(Scanner sc) {
        // Clear any leftover newline characters in the buffer
        sc.nextLine(); // This will consume the remaining newline from any previous input

        while (true) {
            System.out.print("Enter product type (PANTS, HAT, COAT, SHIRT, SHOES): ");
            String input = sc.nextLine().trim().toUpperCase(); // Read and format the input

            // Check if the input is empty
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid product type.");
                continue; // Skip this iteration and ask for input again
            }

            // Check if input is valid
            try {
                return ProductType.valueOf(input); // Try to convert input to ProductType enum
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid product type. Please select a valid option.");
            }
        }
    }

    private String validateDescription(Scanner sc) {
        while (true) {
            System.out.print("Enter product description (up to 100 characters, at least one letter): ");
            String description = sc.nextLine().trim();
            if (description.matches(".*[A-Za-z].*") && description.length() <= 100) {
                return description;
            }
            System.out.println("Invalid description. Ensure it meets the criteria.");
        }
    }

    private int validateAdditionalQuantity(Scanner sc, Product existingProduct, int maxQuantity) {
        while (true) {
            System.out.printf("How much do you want to add from '%s'? ", existingProduct.getProductName());
            if (sc.hasNextInt()) {
                int quantity = sc.nextInt();
                if (quantity > 0 && quantity <= maxQuantity) {
                    return quantity;
                }
                System.out.printf("Invalid quantity. You can add up to %d units.%n", maxQuantity);
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); // Clear invalid input
            }
        }
    }

    private void updateProductPrice() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** UPDATE PRODUCT PRICE *****");

        System.out.print("Enter the product ID to update its price: ");
        int productId;
        try {
            productId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid numeric ID.");
            return;
        }

        Product product = productManagementService.getProductById(productId);
        if (product == null) {
            System.out.println("No product found with the provided ID. Please try again.");
            return;
        }

        System.out.print("Enter the new price for the product: ");
        double newPrice;
        try {
            newPrice = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please enter a numeric value.");
            return;
        }

        if (newPrice <= 0) {
            System.out.println("Price must be greater than 0.");
            return;
        }

        double oldPrice = product.getPrice();
        product.setPrice(newPrice);
        System.out.printf("Product price updated from $%.2f to $%.2f%n", oldPrice, newPrice);
    }

    private void updateProductDescription() {
        Scanner sc = new Scanner(System.in);

        System.out.println("***** UPDATE PRODUCT DESCRIPTION *****");

        System.out.print("Enter the product ID to update its description: ");
        int productId;
        try {
            productId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid numeric ID.");
            return;
        }

        Product product = productManagementService.getProductById(productId);
        if (product == null) {
            System.out.println("No product found with the provided ID. Please try again.");
            return;
        }

        System.out.print("Enter the new description for the product: ");
        String newDescription = sc.nextLine();

        if (newDescription == null || newDescription.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        product.setDescription(newDescription);
        System.out.println("Product description updated successfully.");
    }

    private void viewPurchaseHistory() {
        User loggedInUser = context.getLoggedInUser();
        if (loggedInUser == null || !loggedInUser.getRole().equals(UserRole.ADMIN)) {
            System.out.println("Access denied. Only admins can view purchase history.");
            return;
        }

        System.out.println("***** PURCHASE HISTORY *****");

        Order[] allOrders = orderManagementService.getOrders();
        if (allOrders == null || allOrders.length == 0) {
            System.out.println("No orders have been placed yet.");
            return;
        }

        for (Order order : allOrders) {
            // Fetch user details
            String customerEmail = order.getCustomerEmail();
            User customer = context.getUserManagementService().getUserByEmail(customerEmail);

            if (customer == null) {
                System.out.println("DEBUG: No user found for email: " + customerEmail);
                continue;
            }

            // Display customer name
            System.out.printf("Order placed by %s %s (%s):%n",
                    customer.getFirstName(), customer.getLastName(), customer.getEmail());

            // Group products by name and calculate total per product
            Map<String, Integer> productQuantities = new HashMap<>();
            Map<String, Double> productTotalPrice = new HashMap<>();

            for (Product product : order.getProducts()) {
                String productName = product.getProductName();
                productQuantities.put(productName, productQuantities.getOrDefault(productName, 0) + 1);
                productTotalPrice.put(productName,
                        productTotalPrice.getOrDefault(productName, 0.0) + product.getPrice());
            }

            // Display grouped product information
            for (String productName : productQuantities.keySet()) {
                int quantity = productQuantities.get(productName);
                double totalPrice = productTotalPrice.get(productName);
                System.out.printf(" - %d %s: $%.2f%n", quantity, productName, totalPrice);
            }

            // Display total bill
            System.out.printf("Total: $%.2f%n", order.getTotalBill());
            System.out.println("--------------------------------");
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** MAIN MENU *****");
        System.out.println("1. Sign Up");
        System.out.println("2. Sign In");
        System.out.println("3. Sign Out");
        System.out.println("4. Product Catalog");
        System.out.println("5. My Orders");
        System.out.println("6. Settings");
        System.out.println("7. Customer List");
        System.out.println("8. Manage Store (Admin Only)");
        System.out.println("9. Purchase History (Admin Only)");
    }
}
