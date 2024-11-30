package com.itbulls.learnit.javacore.oop.exam.onlineshop.configs;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Product;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.UserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.DefaultUserManagementService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.UserManagementServiceProxy;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.Cart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.impl.DefaultCart;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.NotificationService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.BasicEmailService;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl.LoggingEmailDecorator;
import com.stripe.Stripe;
import io.github.cdimascio.dotenv.Dotenv;

// This file manages global settings and services, including the Singleton Pattern for shared instances.

public class ApplicationContext {

    private static final Dotenv dotenv = Dotenv.load(); // Load environment variables
    private static ApplicationContext instance;
    private UserManagementService userManagementService;
    private User loggedInUser;
    private Cart sessionCart;
    private EmailService emailService;
    private NotificationService notificationService;
    private double storeBudget = 100.0;
    private boolean welcomeEmailSent = false;

    private ApplicationContext() {
        userManagementService = new UserManagementServiceProxy(DefaultUserManagementService.getInstance());
        sessionCart = new DefaultCart();
        emailService = new LoggingEmailDecorator(new BasicEmailService()); // Decorated EmailService
        notificationService = new NotificationService();
        initializeStripe();
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

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

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public double getStoreBudget() {
        return storeBudget;
    }

    public void updateStoreBudget(double amount) {
        this.storeBudget += amount;
    }

    private void initializeStripe() {
        Stripe.apiKey = dotenv.get("STRIPE_API_KEY");
    }

    public void sendBudgetUpdateEmail(double amount, double newBudget, Product product) {
        if (product == null) {
            return;
        }

        User[] users = getUserManagementService().getUsers();
        String subject = "Store Budget Updated";
        String body = String.format(
            "The store budget has been updated by $%.2f.\nCurrent budget: $%.2f.\n",
            amount, newBudget
        );

        boolean emailSent = false;
        for (User user : users) {
            if (user.getRole() == UserRole.ADMIN) {
                getEmailService().sendEmail(user.getEmail(), subject, body);
                emailSent = true;
            }
        }

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

    public String createAndRegisterUser(UserRole role, String firstName, String lastName, String email, String password) {
        return userManagementService.createAndRegisterUser(role, firstName, lastName, email, password);
    }
}
