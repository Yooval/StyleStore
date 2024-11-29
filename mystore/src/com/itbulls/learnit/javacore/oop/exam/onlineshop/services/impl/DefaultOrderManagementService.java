package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.OrderManagementService;

import java.util.ArrayList;
import java.util.List;

// This file implements OrderManagementService with default operations.

public class DefaultOrderManagementService implements OrderManagementService {

    private static DefaultOrderManagementService instance;
    private List<Order> orders = new ArrayList<>();

    private DefaultOrderManagementService() {}

    public static DefaultOrderManagementService getInstance() {
        if (instance == null) {
            instance = new DefaultOrderManagementService();
        }
        return instance;
    }

    @Override
    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
        } else {
            System.out.println("DEBUG: Attempted to add a null order. Operation ignored.");
        }
    }

    @Override
    public Order[] getOrdersByUserId(int userId) {
        return orders.stream()
                .filter(order -> order.getCustomerId() == userId)
                .toArray(Order[]::new);
    }

    @Override
    public Order[] getOrders() {
        return orders.toArray(new Order[0]);
    }

    @Override
    public Order getOrderById(int orderId) {
        // Using hashCode as a simple unique identifier for orders
        return orders.stream()
                .filter(order -> order.hashCode() == orderId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveOrder(Order order) {
        if (order != null) {
            orders.add(order);
        } else {
            System.out.println("DEBUG: Attempted to save a null order. Operation ignored.");
        }
    }
}
