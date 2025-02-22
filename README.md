# StyleShop  

## Overview  
**StyleShop** is an Object-Oriented Programming (OOP) application built using design pattern principles to create a virtual apparel store. The app supports three user roles: **Admins**, **Sellers**, and **Customers**, each with specific permissions.  

## User Roles and Permissions  

- **Admins**: Manage inventory and process product returns at 80% value. View all completed transactions and oversee store operations.  
- **Sellers**: Assist Customers and earn credit points (10% of bill) redeemable in-store.  
- **Customers**: Browse and shop independently or request assistance from Sellers. Refund purchases within 10 minutes of completing a transaction.  

## Key Features  

- **Email System**: Sends sign-up emails, inventory updates, and purchase receipts.  
- **Loyalty & Rewards**: Shop workers get a 20% discount; Sellers earn credits when assisting Customers.  

## Tools  

- **Email Service (SMTP via Gmail)**: Sends automated emails for user actions and notifications.  
- **Payment Gateway (Stripe API)**: Processes secure credit card transactions with error handling.  

## Installation

1. **Create a `.env` File**: Include sensitive data, such as:
   - Email address
   - Email password (for authenticating the app to send emails)
   - Stripe API key
2. **Download the Project**: Clone or download the repository.
3. **Run the Application**:
   - Navigate to the project folder.
   - Execute the `main.java` file to start the app(**myproject -> src -> Main.java -> Run As -> Java Application**).
