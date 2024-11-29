package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;

public interface OrderManagementService {
    void addOrder(Order order);

    Order[] getOrdersByUserId(int userId);

    Order[] getOrders();

    Order getOrderById(int orderId); // Method to fetch order by ID

	void saveOrder(Order order);
}
