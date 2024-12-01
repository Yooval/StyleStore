# StyleShop

## Overview
**StyleShop** is an Object-Oriented Programming (OOP) application built using design pattern principles to create a virtual clothing shop. The app supports three user roles: **Admins**, **Sellers**, and **Customers**, each with specific permissions. All users can sign up, log in, and interact with the shop, while Admins manage the inventory, and Sellers assist customers.  

The app features a detailed mail system for sign-ups (all users), inventory changes (Admins), and purchase receipts (all users). A **rewards system** provides a 20% discount for shop workers and credits for Sellers who assist Customers. Additionally, all users can refund a purchase (delete a transaction) within 10 minutes.  

Admins can also process product returns at 80% of their value and view all completed transactions, while Sellers earn credit points equal to 10% of the bill when assisting customers, which can be redeemed in-store. Customers can shop independently or request assistance from Sellers.

## Features

### Email Service
- **Tool**: EmailService (SMTP via Gmail)
- **Purpose**: Sends emails to users, such as welcome emails, purchase receipts, and notifications. Utilizes SMTP with Gmail for sending emails.

### Payment Gateway (Stripe)
- **Tool**: Stripe API
- **Purpose**: Handles payment processing, allowing users to make purchases with credit cards. Includes features like payment confirmation and error handling.

## Design Patterns
1. **Singleton Pattern**: Ensures that only one instance of certain global services, like managing application-wide settings or sending emails, is created and used throughout the app.
2. **Factory Pattern**: Simplifies creating different types of users (Admins, Sellers, or Customers) by dynamically generating the correct user type based on the role, making the process efficient and scalable.
3. **Decorator Pattern**: Adds extra functionality to existing services, such as tracking logs when emails are sent, without altering the core behavior of the original service.
4. **Strategy Pattern**: Allows the app to dynamically choose different payment methods at runtime, such as using credit cards or loyalty points, depending on the user's preferences.
5. **Observer Pattern**: Automatically notifies users about important updates, like changes to the store or inventory, by sending notifications or emails, reducing the need for manual intervention.
6. **Proxy Pattern**: Regulates access to sensitive parts of the app, such as managing users, by providing additional security and functionality without changing the underlying service.

## Installation

1. **Create a `.env` File**: Include sensitive data, such as:
   - Email address
   - Email password (for authenticating the app to send emails)
   - Stripe API key
2. **Download the Project**: Clone or download the repository.
3. **Run the Application**:
   - Navigate to the project folder.
   - Execute the `main.java` file to start the app.
