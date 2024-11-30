package com.itbulls.learnit.javacore.oop.exam.onlineshop.services.impl;

import com.itbulls.learnit.javacore.oop.exam.onlineshop.services.EmailService;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

//This file implements the core logic for sending emails.

public class BasicEmailService implements EmailService {

    private static final String HOST = "smtp.gmail.com";
    private static final Dotenv dotenv = Dotenv.load();
    private static final String FROM_EMAIL = dotenv.get("EMAIL_FROM");
    private static final String PASSWORD = dotenv.get("EMAIL_PASSWORD");

    @Override
    public void sendEmail(String recipientEmail, String subject, String messageText) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
