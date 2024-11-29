package com.itbulls.learnit.javacore.oop.exam.onlineshop.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * EmailService handles sending email notifications and acts as an observer in the Observer Pattern.
 */
public class EmailService implements Observer {

    private static final String HOST = "smtp.gmail.com";
    private static final Dotenv dotenv = Dotenv.load(); // Load environment variables

    private static final String FROM_EMAIL = dotenv.get("EMAIL_FROM"); // Your email address (sender's email) from .env file
    private static final String PASSWORD = dotenv.get("EMAIL_PASSWORD"); // Your email password from .env file

    private List<String> recipients; // List of email recipients

    public EmailService() {
        this.recipients = new ArrayList<>();
    }

    /**
     * Subscribe a recipient to the email service.
     * 
     * @param email Recipient's email address.
     */
    public void subscribe(String email) {
        if (!recipients.contains(email)) {
            recipients.add(email);
        }
    }

    /**
     * Unsubscribe a recipient from the email service.
     * 
     * @param email Recipient's email address.
     */
    public void unsubscribe(String email) {
        recipients.remove(email);
    }

    /**
     * Notify all subscribed recipients with a message.
     * 
     * @param subject Subject of the email.
     * @param messageText Message content of the email.
     */
    public void notifyAllRecipients(String subject, String messageText) {
        for (String recipient : recipients) {
            sendEmail(recipient, subject, messageText);
        }
    }

    /**
     * Send a single email.
     * 
     * @param recipientEmail Recipient's email address.
     * @param subject Subject of the email.
     * @param messageText Message content of the email.
     */
    public void sendEmail(String recipientEmail, String subject, String messageText) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.port", "587"); // Port for sending email
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true"); // Enable TLS (security)

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

    @Override
    public void update(String message) {
        // Notify all recipients with the update message
        notifyAllRecipients("System Notification", message);
    }
}
