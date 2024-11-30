package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

// This file defines the interface for email services.

public interface EmailService {
    void sendEmail(String recipientEmail, String subject, String messageText);
}
