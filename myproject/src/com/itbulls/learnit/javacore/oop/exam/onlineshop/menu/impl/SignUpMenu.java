package com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.impl;

import java.util.Scanner;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.configs.ApplicationContext;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.UserRole;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.enteties.User;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.menu.Menu;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.utils.InputValidator;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.factories.UserFactory;
import com.itbulls.learnit.javacore.oop.exam.onlineshop.factories.impl.DefaultUserFactory;

public class SignUpMenu implements Menu {

    private ApplicationContext context;

    {
        context = ApplicationContext.getInstance();
    }

    @Override
    public void start() {
        printMenuHeader();
        Scanner sc = new Scanner(System.in);

        String firstName = getValidName(sc, "Please, enter your first name: ");
        String lastName = getValidName(sc, "Please, enter your last name: ");
        String password = getValidPassword(sc, "Please, enter your password: ");
        String email = getValidEmail(sc, "Please, enter your email: ");

        UserRole role = null;
        while (role == null) {
            System.out.print("Please, enter the role (CUSTOMER, ADMIN, or SELLER): ");
            String roleInput = sc.nextLine().trim().toUpperCase();
            try {
                role = UserRole.valueOf(roleInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please enter either CUSTOMER, ADMIN, or SELLER.");
            }
        }

        // Use the factory to create a user
        UserFactory userFactory = new DefaultUserFactory();
        User user = userFactory.createUser(role, firstName, lastName, email, password);

        String errorMessage = context.getUserManagementService().registerUser(user);

        if (errorMessage == null || errorMessage.isEmpty()) {
            context.setLoggedInUser(user);
            System.out.println("New user is created.");
         // Send welcome email
            context.getEmailService().sendEmail(
                    user.getEmail(),
                    "Welcome to the Online Store",
                    "Dear " + user.getFirstName()
                            + ",\n\nWelcome to the Online Store!\n\nBest regards,\nThe Store Team.");
        }
        }

    private String getValidName(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (InputValidator.isValidName(input)) {
                return input;
            } else {
                System.out.println("Invalid name. Only letters are allowed, and it must be at least 2 characters long.");
            }
        }
    }

    private String getValidEmail(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (InputValidator.isValidEmail(input)) {
                return input;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }
    }

    private String getValidPassword(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            if (InputValidator.isValidPassword(input)) {
                return input;
            } else {
                System.out.println("Invalid password. It must be at least 6 characters long.");
            }
        }
    }

    @Override
    public void printMenuHeader() {
        System.out.println("***** SIGN UP *****");
    }
}
