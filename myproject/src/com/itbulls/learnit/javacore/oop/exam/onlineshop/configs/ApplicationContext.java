package com.itbulls.learnit.javacore.oop.exam.onlineshop.configs;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.UserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultUserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Cart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultCart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationService;
import com.stripe.Stripe;
import io.github.cdimascio.dotenv.Dotenv;

// This file manages global application settings, including logged-in users and shared services.

public class ApplicationContext {

    private static final Dotenv dotenv = Dotenv.load(); // Load environment variables
    private static ApplicationContext instance;
    private UserManagementService userManagementService;
    private User loggedInUser;
    private Cart sessionCart;
    private EmailService emailService;
    private NotificationService notificationService;
    private double storeBudget = 100.0;

    private ApplicationContext() {
        userManagementService = DefaultUserManagementService.getInstance();
        sessionCart = new DefaultCart();
        emailService = new EmailService();
        notificationService = new NotificationService();
        initializeStripe(); // Initialize Stripe with .env data
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private boolean welcomeEmailSent = false;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if (loggedInUser != null && !welcomeEmailSent) {
            sendWelcomeEmail();
            welcomeEmailSent = true;
        }
    }

    public Cart getSessionCart() {
        return sessionCart;
    }

    public void clearSessionCart() {
        sessionCart.clear();
    }

    public EmailService getEmailService() {
        return emailService;
    }

    private void initializeStripe() {
        // Initialize Stripe API key from the .env file
        Stripe.apiKey = dotenv.get("STRIPE_API_KEY");
    }

    public double getStoreBudget() {
        return storeBudget;
    }

    public void updateStoreBudget(double amount) {
        this.storeBudget += amount;
    }

    public void sendBudgetUpdateEmail(double amount, double newBudget, Product product) {
        if (product == null) {
            return;
        }

        // Get all users
        User[] users = getUserManagementService().getUsers();

        // Construct the email subject and body
        String subject = "Store Budget Updated";
        String body = String.format(
            "The store budget has been updated by $%.2f.\n" +
            "Current budget: $%.2f.\n" +
            "New Product Details:\n" +
            " - Name: %s\n" +
            " - Type: %s\n" +
            " - Quantity: %d\n" +
            " - Selling Price: $%.2f\n" +
            " - Description: %s\n",
            amount, newBudget, 
            product.getProductName(), product.getType(), product.getQuantity(),
            product.getPrice(), product.getDescription()
        );

        // Send the email to all admins
        boolean emailSent = false;
        for (User user : users) {
            if (user.getRole() == UserRole.ADMIN) {
                // Send the email to each admin
                getEmailService().sendEmail(user.getEmail(), subject, body);
                emailSent = true;
            }
        }

        // If no admins are found, send to a default email
        if (!emailSent) {
            String defaultAdminEmail = "default_admin@example.com";
            getEmailService().sendEmail(defaultAdminEmail, subject, body);
        }
    }


    private void sendWelcomeEmail() {
        if (loggedInUser == null || loggedInUser.getEmail() == null) {
            return;
        }

        String subject = "Welcome to the Online Store";
        String body = String.format(
            "Dear %s,\n\nWelcome to the Online Store!\n\nBest regards,\nThe Online Store Team",
            loggedInUser.getFirstName()
        );

        getEmailService().sendEmail(loggedInUser.getEmail(), subject, body);
    }

    public UserManagementService getUserManagementService() {
        return DefaultUserManagementService.getInstance();
    }

    public String createAndRegisterUser(UserRole role, String firstName, String lastName, String email, String password) {
        return userManagementService.createAndRegisterUser(role, firstName, lastName, email, password);
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
