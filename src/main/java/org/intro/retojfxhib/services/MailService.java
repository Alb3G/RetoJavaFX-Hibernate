package org.intro.retojfxhib.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService implements Runnable {
    private String userName = "mymovieshelf615@gmail.com";
    private String password = System.getenv("MAIL_PASSWORD");
    private String userEmail;
    private String securityCode;

    public MailService(String userEmail, String securityCode) {
        this.userEmail = userEmail;
        this.securityCode = securityCode;
    }

    @Override
    public void run() {
        // Set up the email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create the email session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mymovieshelf615@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Verification Code");
            message.setText("Your verification code is: " + securityCode);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
