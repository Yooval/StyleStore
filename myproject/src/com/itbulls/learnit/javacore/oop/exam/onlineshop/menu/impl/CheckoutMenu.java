package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Cart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Order;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.ReceiptItem;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultOrder;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.Seller;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.PaymentStrategy;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.CreditCardPayment;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.PointsPayment;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultOrderManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultUserManagementService;

import java.util.*;

// This file implements the Strategy Pattern for dynamic payment handling (PointsPayment, CreditCardPayment

public class CheckoutMenu implements Menu {

    private ApplicationContext context;
    private EmailService emailService;
    private PaymentStrategy paymentStrategy;

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

        if (context.getLoggedInUser().getRole() == UserRole.SELLER
                || context.getLoggedInUser().getRole() == UserRole.ADMIN) {
            totalCost *= 0.80;
            System.out.println("Because you are working with us, you get a 20% discount!");
            System.out.printf("Your new total is $%.2f after applying the discount.\n", totalCost);
        }

        Scanner sc = new Scanner(System.in);
        Seller seller = null;
        double pointsToUse = 0;

        if (context.getLoggedInUser() instanceof Seller) {
            seller = (Seller) context.getLoggedInUser();
            if (seller.getCreditPoints() > 0) {
                System.out.printf("You have %.2f points.\n", seller.getCreditPoints());

                double maxDiscount = totalCost * 0.50;
                double maxPoints = Math.min(seller.getCreditPoints(), maxDiscount);

                System.out.printf("You can use up to %.2f points for this purchase. Enter the number of points to use (or 0 to skip): ",
                        maxPoints);
                pointsToUse = sc.nextDouble();
                sc.nextLine();

                if (pointsToUse > 0 && pointsToUse <= maxPoints) {
                    setPaymentStrategy(new PointsPayment(seller, pointsToUse));
                    totalCost -= pointsToUse;
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

        System.out.print("Enter your credit card number without spaces and press enter to confirm purchase: ");
        String creditCardNumber = sc.nextLine();
        setPaymentStrategy(new CreditCardPayment(creditCardNumber));

        if (!paymentStrategy.pay(totalCost)) {
            System.out.println("Payment failed. Please try again.");
            if (seller != null && pointsToUse > 0) {
                seller.setCreditPoints(seller.getCreditPoints() + pointsToUse);
                System.out.printf("Your points have been restored. You now have %.2f points.\n", seller.getCreditPoints());
            }

            for (Product product : new HashSet<>(sessionCart.getProducts())) {
                int purchasedQuantity = sessionCart.getProductQuantity(product);
                int currentQuantity = product.getQuantity();
                product.setQuantity(currentQuantity + purchasedQuantity);
            }

            return;
        }

        Product[] productsArray = sessionCart.getProducts().toArray(new Product[0]);
        Order order = createOrder(context.getLoggedInUser().getId(), productsArray, creditCardNumber);
        DefaultOrderManagementService.getInstance().saveOrder(order);

        context.updateStoreBudget(totalCost);
        sendReceiptEmail(productsArray, totalCost);
        sendAdminEmail(order, context.getStoreBudget());
        
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

        sessionCart.clear();
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


    private void sendReceiptEmail(Product[] products, double totalCost) {
        User loggedInUser = context.getLoggedInUser();
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append(String.format("Dear %s,\n\n", loggedInUser.getFirstName()));
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

        emailService.sendEmail(loggedInUser.getEmail(), "Your Purchase Receipt", receiptBuilder.toString());
    }

    private void sendAdminEmail(Order order, double updatedBudget) {
        User[] users = context.getUserManagementService().getUsers();
        String subject = "New Purchase Details";
        String body = String.format(
                "New Purchase Details:\nCustomer Email: %s\nOrder Total: $%.2f\nUpdated Store Budget: $%.2f\n",
                order.getCustomerEmail(), order.getTotalBill(), updatedBudget);

        boolean emailSent = false;
        for (User user : users) {
            if (user.getRole() == UserRole.ADMIN) {
                emailService.sendEmail(user.getEmail(), subject, body);
                emailSent = true;
            }
        }

        if (!emailSent) {
            String defaultAdminEmail = "default_admin@example.com";
            emailService.sendEmail(defaultAdminEmail, subject, body);
        }
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

    @Override
    public void printMenuHeader() {
        System.out.println("***** CHECKOUT MENU *****");
    }
}
