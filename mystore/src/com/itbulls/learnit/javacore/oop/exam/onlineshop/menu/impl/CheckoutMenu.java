package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultOrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultProductManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultUserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.*;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultOrder;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Admin;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.PaymentStrategy;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.CreditCardPayment;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.PointsPayment;

import java.util.*;

// This file handles the checkout process, including payments and seller interactions.

public class CheckoutMenu implements Menu {
    private ApplicationContext context;
    private EmailService emailService;
    private PaymentStrategy paymentStrategy; // Strategy Pattern for payment

    public CheckoutMenu() {
        context = ApplicationContext.getInstance();
        emailService = context.getEmailService();
    }

    @Override
    public void start() {
        System.out.println("***** CHECKOUT MENU *****");
        Cart sessionCart = context.getSessionCart();
        if (sessionCart == null || sessionCart.isEmpty()) {
            System.out.println("Your cart is empty. Please add products before proceeding to checkout.");
            return;
        }
        double totalCost = sessionCart.getTotalBill();
        System.out.printf("Your total is $%.2f.\n", totalCost);

        // Apply 20% discount for Admins and Sellers
        if (context.getLoggedInUser().getRole() == UserRole.SELLER
                || context.getLoggedInUser().getRole() == UserRole.ADMIN) {

            totalCost *= 0.80; // Apply 20% discount
            System.out.println("Because you are working with us, you get a 20% discount!");
            System.out.printf("Your new total is $%.2f after applying the discount.\n", totalCost);
        }

        Scanner sc = new Scanner(System.in);
        Seller seller = null;
        double pointsToUse = 0;
        // Step 1: Check if logged-in user is a seller and allow them to use points
        if (context.getLoggedInUser() instanceof Seller) {
            seller = (Seller) context.getLoggedInUser();
            if (seller.getCreditPoints() > 0) {
                System.out.printf("You have %.2f points.\n", seller.getCreditPoints());

                double maxDiscount = totalCost * 0.50; // Maximum 50% of the total bill
                double maxPoints = Math.min(seller.getCreditPoints(), maxDiscount); // Cap points to the max allowable
                                                                                    // discount

                System.out.printf(
                        "You can use up to %.2f points for this purchase. Enter the number of points to use (or 0 to skip): ",
                        maxPoints);
                pointsToUse = sc.nextDouble();
                sc.nextLine(); // Consume the newline character

                // Validate the entered points
                if (pointsToUse > 0 && pointsToUse <= maxPoints) {
                    setPaymentStrategy(new PointsPayment(seller, pointsToUse)); // Use points for payment
                    totalCost -= pointsToUse; // Adjust the total cost
                    seller.setCreditPoints(seller.getCreditPoints() - pointsToUse);
                    System.out.printf("You used %.2f points (you have %.2f remaining). Your new total is $%.2f.\n",
                            pointsToUse, seller.getCreditPoints(), totalCost);
                } else if (pointsToUse == 0) {
                    System.out.println("Points not used. Proceeding with the full amount.");
                } else {
                    System.out.println("Invalid input. Points not used.");
                }
            } else {
                System.out.println("You don't have any points to use. Proceeding with the full amount.");
            }
        }

        // Step 2: Ask for Credit Card Information
        System.out.print("Enter your credit card number without spaces and press enter to confirm purchase: ");
        String creditCardNumber = sc.nextLine();
        setPaymentStrategy(new CreditCardPayment(creditCardNumber)); // Set credit card as payment method

        // Step 3: Verify Payment
        if (!paymentStrategy.pay(totalCost)) {
            System.out.println("Payment failed. Please try again.");
            if (seller != null && pointsToUse > 0) {
                seller.setCreditPoints(seller.getCreditPoints() + pointsToUse); // Restore the points back to the seller
                System.out.printf("Your points have been restored. You now have %.2f points.\n",
                        seller.getCreditPoints());
            }
            // Step 4: Restore the original product quantities back to the store if payment
            // failed
            // We use a Set to ensure each product is processed only once.
            Set<Product> uniqueProducts = new HashSet<>(sessionCart.getProducts());

            for (Product product : uniqueProducts) {
                int purchasedQuantity = sessionCart.getProductQuantity(product);
                int currentQuantity = product.getQuantity();

                // Print the values of current and purchased quantities for debugging
                System.out.println("Debugging values for product: " + product.getProductName());
                System.out.printf("Current Quantity of %s: %d\n", product.getProductName(), currentQuantity);
                System.out.printf("Purchased Quantity of %s: %d\n", product.getProductName(), purchasedQuantity);

                // Restore the product quantity to what it was before the purchase attempt
                product.setQuantity(currentQuantity + purchasedQuantity); // Add the purchased quantity back
            }

            return; // Exit the method if payment failed
        }

        else {
            // Step 4: Save Order
            Product[] productsArray = sessionCart.getProducts().toArray(new Product[0]);
            Order order = createOrder(context.getLoggedInUser().getId(), productsArray, creditCardNumber);
            DefaultOrderManagementService.getInstance().saveOrder(order);

            // Step 5: Update Store Budget
            context.updateStoreBudget(totalCost);

            // Step 6: Send Receipt Email to Customer
            sendReceiptEmail(productsArray, totalCost);

            // Step 7: Notify Admin About Purchase
            sendAdminEmail(order, context.getStoreBudget());

            // Step 8: Ask for Seller Assistance if Customer
            if (context.getLoggedInUser().getRole() == UserRole.CUSTOMER) {
                // Fetch the list of available sellers
                List<Seller> sellers = displaySellers();

                if (!sellers.isEmpty()) {
                    System.out.println("Did a seller assist you during your purchase? (yes/no)");
                    String response = sc.nextLine().trim().toLowerCase();

                    if (response.equals("yes")) {
                        System.out.println("Choose a seller from the list above:");
                        System.out.print("Enter the number corresponding to the seller who assisted you: ");
                        int sellerChoice = sc.nextInt();
                        sc.nextLine(); // Consume newline

                        if (sellerChoice > 0 && sellerChoice <= sellers.size()) {
                            Seller selectedSeller = sellers.get(sellerChoice - 1);
                            selectedSeller.helpCustomer(totalCost);
                            System.out.println("Seller " + selectedSeller.getFirstName() + " "
                                    + selectedSeller.getLastName() + " has been credited.");
                        } else {
                            System.out.println("Invalid seller choice. No points credited.");
                        }
                    } else {
                        System.out.println("Thank you for your purchase!");
                    }
                } else {
                    System.out.println("Thank you for your purchase!");
                }
                ApplicationContext.getInstance().sendBudgetUpdateEmail(
                        totalCost,
                        ApplicationContext.getInstance().getStoreBudget(),
                        null // Null indicates an aggregate purchase update email
                );
            }
            // Step 9: Clear Cart
            sessionCart.clear();
        }

    }

    private List<Seller> displaySellers() {
        List<User> users = Arrays.asList(DefaultUserManagementService.getInstance().getUsers());
        List<Seller> sellers = new ArrayList<>();

        // Filter only SELLER users
        for (User user : users) {
            if (user.getRole() == UserRole.SELLER && user instanceof Seller) {
                sellers.add((Seller) user);
            }
        }

        // Display the sellers
        if (sellers.isEmpty()) {
            System.out.println("No sellers are available at the moment.");
            return sellers;
        }

        System.out.println("***** SELLERS *****");
        for (int i = 0; i < sellers.size(); i++) {
            Seller seller = sellers.get(i);
            System.out.printf("%d. %s %s\n", i + 1, seller.getFirstName(), seller.getLastName());
        }

        return sellers;
    }

    private void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    private Order createOrder(int userId, Product[] products, String creditCardNumber) {
        Order order = new DefaultOrder();
        order.setCustomerId(userId);
        order.setProducts(products);
        order.setCreditCardNumber(creditCardNumber);
        return order;
    }

    private void sendReceiptEmail(Product[] products, double totalCost) {
        User loggedInUser = context.getLoggedInUser();
        String userName = loggedInUser.getFirstName();
        String customerEmail = loggedInUser.getEmail();

        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append(String.format("Dear %s,\n\n", userName));
        receiptBuilder.append("Thank you for your purchase! Here are the details:\n\n");

        Map<String, ReceiptItem> receiptItems = new HashMap<>();
        for (Product product : products) {
            String productName = product.getProductName();
            receiptItems.putIfAbsent(productName, new ReceiptItem(0, product.getPrice(), 0));
            ReceiptItem item = receiptItems.get(productName);
            item.quantity++;
            item.totalPrice += product.getPrice();
        }

        for (Map.Entry<String, ReceiptItem> entry : receiptItems.entrySet()) {
            ReceiptItem item = entry.getValue();
            receiptBuilder.append(String.format(" - %d x %s: $%.2f (Total: $%.2f)\n",
                    item.quantity, entry.getKey(), item.unitPrice, item.totalPrice));
        }

        receiptBuilder.append(String.format("\nTotal Amount Paid: $%.2f\n", totalCost));
        receiptBuilder.append("\nThank you for shopping with us!");

        emailService.sendEmail(customerEmail, "Your Purchase Receipt", receiptBuilder.toString());
    }

    private void sendAdminEmail(Order order, double updatedBudget) {
        // Get the list of all registered users
        User[] users = context.getUserManagementService().getUsers();

        // Find all admins and send emails
        String subject = "New Purchase Details";
        String body = String.format(
                "New Purchase Details:\n" +
                        "Customer Email: %s\n" +
                        "Order Total: $%.2f\n" +
                        "Updated Store Budget: $%.2f\n",
                order.getCustomerEmail(), order.getTotalBill(), updatedBudget);

        boolean emailSent = false; // Track if at least one email is sent
        for (User user : users) {
            if (user.getRole() == UserRole.ADMIN) {
                emailService.sendEmail(user.getEmail(), subject, body);
                emailSent = true;
            }
        }

        // If no admins are found, send to a default email
        if (!emailSent) {
            String defaultAdminEmail = "default_admin@example.com";
            emailService.sendEmail(defaultAdminEmail, subject, body);
            System.out.println("No admins found. Default admin notification sent to: " + defaultAdminEmail);
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** CHECKOUT MENU *****");
    }
}
