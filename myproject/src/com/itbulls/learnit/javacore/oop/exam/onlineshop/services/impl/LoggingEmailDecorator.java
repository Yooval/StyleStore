package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;
// This file adds logging functionality to EmailService using the Decorator Pattern.
public class LoggingEmailDecorator implements EmailService {

    private EmailService wrappedEmailService;

    public LoggingEmailDecorator(EmailService emailService) {
        this.wrappedEmailService = emailService;
    }

    @Override
    public void sendEmail(String recipientEmail, String subject, String messageText) {
        System.out.println("LOG: Sending email to " + recipientEmail);
        wrappedEmailService.sendEmail(recipientEmail, subject, messageText);
    }
}
