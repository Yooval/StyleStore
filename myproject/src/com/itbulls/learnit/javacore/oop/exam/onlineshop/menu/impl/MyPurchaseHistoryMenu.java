package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.OrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultOrderManagementService;

// This file displays a logged-in user's purchase history.

public class MyPurchaseHistoryMenu implements Menu {

    private ApplicationContext context;
    private OrderManagementService orderManagementService;

    {
        context = ApplicationContext.getInstance();
        orderManagementService = DefaultOrderManagementService.getInstance();
    }

    @Override
    public void start() {
        printMenuHeader();

        if (context.getLoggedInUser() == null) {
            System.out.println("Please log in to view your purchase history.");
            return;
        }

        int userId = context.getLoggedInUser().getId();
        Order[] userOrders = orderManagementService.getOrdersByUserId(userId);

        if (userOrders == null || userOrders.length == 0) {
            System.out.println("You have no purchase history.");
        } else {
            for (Order order : userOrders) {
                System.out.printf("Order Total: $%.2f%n", order.getTotalBill());
                System.out.println("Products:");
                for (Product product : order.getProducts()) {
                    System.out.printf(" - %s: $%.2f%n", product.getProductName(), product.getPrice());
                }
                System.out.println("--------------------------------");
            }
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** PURCHASE HISTORY *****");
    }
}
