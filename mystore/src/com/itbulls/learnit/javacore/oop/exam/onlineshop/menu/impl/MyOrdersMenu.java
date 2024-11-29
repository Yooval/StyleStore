package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.OrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultProductManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultOrderManagementService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// This file allows users to view and manage their orders.

public class MyOrdersMenu implements Menu {

    private ApplicationContext context;
    private OrderManagementService orderManagementService;

    {
        context = ApplicationContext.getInstance();
        orderManagementService = DefaultOrderManagementService.getInstance();
    }

    @Override
    public void start() {
        System.out.println("***** MY ORDERS *****");

        User loggedInUser = context.getLoggedInUser();
        if (loggedInUser == null) {
            System.out.println("Please log in or create an account to view your orders.");
            new MainMenu().start();
            return;
        }

        // Show the logged-in user's orders
        printUserOrders(loggedInUser);

        // Prompt for refund option
        handleRefundOption(loggedInUser);
    }

    private void printUserOrders(User loggedInUser) {
        Order[] userOrders = orderManagementService.getOrdersByUserId(loggedInUser.getId());

        if (userOrders == null || userOrders.length == 0) {
            System.out.println("You don't have any orders yet.");
            return;
        }

        System.out.println("Your Orders:");
        for (Order order : userOrders) {
            System.out.printf("Order ID: %d, Total: $%.2f, Timestamp: %s%n", order.hashCode(), order.getTotalBill(),
                    order.getTimestamp());

            // Show refund eligibility based on user role and timestamp
            if (loggedInUser.getRole() == UserRole.ADMIN) {
                System.out.println("Refund eligible (admin override).");
            } else {
                System.out.println(
                        order.isRefundEligible() ? "Refund eligible (within 10 minutes)." : "Refund not eligible.");
            }
        }
    }

    private void handleRefundOption(User loggedInUser) {
        System.out.print("Enter the order ID to request a refund, or type 'back' to return: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if (input.equalsIgnoreCase("back")) {
            new MainMenu().start(); // Return to the main menu
            return;
        }

        try {
            int orderId = Integer.parseInt(input);
            Order order = orderManagementService.getOrderById(orderId);
            if (order != null) {
                processRefund(order, loggedInUser);
            } else {
                System.out.println("Invalid order ID. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid order ID.");
        }
    }

    private void processRefund(Order order, User loggedInUser) {
        if (order.isRefunded()) {
            System.out.println("This order has already been refunded.");
            return;
        }

        if (!order.isRefundEligible() && loggedInUser.getRole() != UserRole.ADMIN) {
            sendRefundDeniedEmail(order, loggedInUser);
            return;
        }

        double refundAmount = order.getTotalBill();

        // Update budget only once
        context.updateStoreBudget(-refundAmount);

        order.markAsRefunded();

        sendRefundToAdmins(order, refundAmount);
        sendRefundToCustomer(order, refundAmount);

        System.out.println("Refund processed successfully.");
    }

    private void sendRefundToAdmins(Order order, double refundAmount) {
        String subject = "Refund Processed - Store Budget Updated";

        StringBuilder body = new StringBuilder();
        body.append("Refund Request Details:\n")
                .append("Refund from: ").append(order.getCustomerEmail()).append("\n")
                .append("Refund Request Date: ").append(java.time.LocalDateTime.now()).append("\n")
                .append("Refund Amount: $").append(String.format("%.2f", refundAmount)).append("\n")
                .append("Updated Store Budget: $").append(String.format("%.2f", context.getStoreBudget()))
                .append("\n\n")
                .append("Items Refunded:\n");

        // Calculate refunded items
        Map<Product, Integer> productRefundMap = calculateRefundedItems(order);

        for (Map.Entry<Product, Integer> entry : productRefundMap.entrySet()) {
            Product product = entry.getKey();
            int refundedQuantity = entry.getValue();
            int totalStockQuantity = product.getQuantity(); // The current stock after the refund

            body.append(" - ").append(product.getProductName())
                    .append(": Quantity Refunded (").append(refundedQuantity)
                    .append("), Total Quantity in Stock (").append(totalStockQuantity + refundedQuantity).append(")\n");
        }

        // Send email to all registered admins
        boolean emailSent = false;
        for (User user : context.getUserManagementService().getUsers()) {
            if (user.getRole() == UserRole.ADMIN) {
                context.getEmailService().sendEmail(user.getEmail(), subject, body.toString());
                emailSent = true;
            }
        }

        // Fallback if no admins found
        if (!emailSent) {
            String defaultAdminEmail = "default_admin@example.com";
            context.getEmailService().sendEmail(defaultAdminEmail, subject, body.toString());
            System.out.println("No registered admins found. Refund notification sent to default admin email: "
                    + defaultAdminEmail);
        }
    }

    private Map<Product, Integer> calculateRefundedItems(Order order) {
        Map<Product, Integer> productRefundMap = new HashMap<>();

        for (Product product : order.getProducts()) {
            productRefundMap.put(product, productRefundMap.getOrDefault(product, 0) + 1);
        }

        return productRefundMap;
    }

    private void sendRefundToCustomer(Order order, double refundAmount) {
        String customerEmail = order.getCustomerEmail();
        String subject = "Your Refund Has Been Approved";

        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(order.getCustomerEmail()).append(",\n\n")
                .append("Your refund has been approved. Here are the details:\n\n")
                .append("Refund Amount: $").append(String.format("%.2f", refundAmount)).append("\n")
                .append("Refund Date: ").append(java.time.LocalDateTime.now()).append("\n\n");

        // Consolidate refunded items
        Map<String, Integer> itemCounts = new HashMap<>();
        for (Product product : order.getProducts()) {
            itemCounts.put(product.getProductName(), itemCounts.getOrDefault(product.getProductName(), 0) + 1);
        }

        body.append("Items Refunded:\n");
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            body.append(" - ").append(entry.getKey()).append(" (x").append(entry.getValue()).append(")\n");
        }

        body.append("\nThank you for shopping with us! If you have any questions, feel free to contact us.");

        context.getEmailService().sendEmail(customerEmail, subject, body.toString());
        System.out.println("Refund notification sent to customer: " + customerEmail);
    }

    private void sendRefundDeniedEmail(Order order, User loggedInUser) {
        String customerEmail = order.getCustomerEmail();
        String subject = "Refund Denied - Store Policy";
        String body = "Dear " + customerEmail + ",\n\n" +
                "Unfortunately, your refund request has been denied due to our store policy. Refunds are only allowed within 10 minutes of purchase.\n\n"
                +
                "We appreciate your understanding.\n\n" +
                "Best regards,\n" +
                "The Online Store Team";

        // Send denial email to customer
        context.getEmailService().sendEmail(customerEmail, subject, body);
        System.out.println("Refund denial notification sent to customer.");
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** MY ORDERS *****");
    }
}