package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.PaymentStrategy;

// This file implements payment using loyalty points for sellers.

public class PointsPayment implements PaymentStrategy {

    private Seller seller;
    private double pointsToUse;

    public PointsPayment(Seller seller, double pointsToUse) {
        this.seller = seller;
        this.pointsToUse = pointsToUse;
    }

    @Override
    public boolean pay(double amount) {
        // Validate points availability
        if (pointsToUse > seller.getCreditPoints()) {
            System.out.println("Insufficient points for this payment.");
            return false;
        }

        if (pointsToUse <= 0) {
            System.out.println("Points to use must be greater than zero.");
            return false;
        }

        // Deduct the points from the seller's balance
        seller.setCreditPoints(seller.getCreditPoints() - pointsToUse);

        // Confirm successful payment
        System.out.printf("Payment of $%.2f made using %.2f points. Remaining points: %.2f.\n",
                pointsToUse, pointsToUse, seller.getCreditPoints());
        return true;
    }
    public void setCreditPoints(double creditPoints) {
        if (creditPoints < 0) {
            throw new IllegalArgumentException("Credit points cannot be negative");
        }
        this.pointsToUse = creditPoints;
    }

}
