package com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties;

// This file represents individual items in a receipt, linking products to quantities.

public class ReceiptItem {
    public int quantity;       // Quantity of the product
    public double unitPrice;   // Price per unit of the product
    public double totalPrice;  // Total price for the quantity

    public ReceiptItem(int quantity, double unitPrice, double totalPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }
}
