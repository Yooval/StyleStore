package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// This file handles the checkout process, including seller interactions.

public class CheckoutService {
    private List<Seller> sellers;

    public CheckoutService() {
        // Initialize sellers (replace with DB logic if needed)
        sellers = new ArrayList<>();
    }

    public void checkout(User user) {
        if (user.getRole() != UserRole.CUSTOMER) {
            System.out.println("Only customers can perform checkout.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("Did a seller assist you during your purchase? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            System.out.println("Here is a list of available sellers:");
            displaySellers();

            System.out.println("Enter the number corresponding to the seller who assisted you:");
            int sellerChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (sellerChoice > 0 && sellerChoice <= sellers.size()) {
                Seller selectedSeller = sellers.get(sellerChoice - 1);
                System.out.println("Thank you! Seller " + selectedSeller.getFirstName() +
                        " " + selectedSeller.getLastName() + " has been credited for assisting you.");
                selectedSeller.helpCustomer(100.0); // Example: Add credit points for a $100 bill
            } else {
                System.out.println("Invalid choice. No seller was credited.");
            }
        } else {
            System.out.println("Thank you for your purchase!");
        }
    }

    private void displaySellers() {
        for (int i = 0; i < sellers.size(); i++) {
            Seller seller = sellers.get(i);
            System.out.println((i + 1) + ". " + seller.getFirstName() + " " + seller.getLastName());
        }
    }
}
