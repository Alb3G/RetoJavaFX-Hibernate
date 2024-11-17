package org.intro.retojfxhib.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Servicio de envío de emails para diferentes confirmaciones,
 * tanto confirmación del registro como de eliminado de cuenta.
 * @author Alberto Guzman
 */
public class MailService implements Runnable {
    private String userName = "mymovieshelf615@gmail.com";
    private String password = System.getenv("MAIL_PASSWORD");
    private String userEmail;
    private String securityCode;

    public MailService(String userEmail, String securityCode) {
        this.userEmail = userEmail;
        this.securityCode = securityCode;
    }

    /**
     * El método run nos permite poder ejecutar el servicio en un Thread diferente al principal,
     * para que la Ui no se frezee mientras se envía el email, esto nos permite que los servicios
     * se ejecuten por detrás del hilo principal mientras la Ui sigue siendo funcional.
     */
    @Override
    public void run() {
        // Set up the email properties
        Properties props = new Properties();
        // Allow smtp server to ask for User and pass for authentication.
        props.put("mail.smtp.auth", "true");
        // Enable TLS (Transport Layer Security) for connection encryptation.
        props.put("mail.smtp.starttls.enable", "true");
        // Host == smtp server that we will use to send emails in this case is Gmail's server
        props.put("mail.smtp.host", "smtp.gmail.com");
        // We specify the port where we want to connect, port 587 is the common port for safe smtp connections with TLS
        props.put("mail.smtp.port", "587");

        // Create the email session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            // Create the email message.
            Message message = new MimeMessage(session);
            // Sender of the email, in this case I have created a personal gmail for MyMovieShelf app.
            message.setFrom(new InternetAddress("mymovieshelf615@gmail.com"));
            // We set the target email that we want to reach with the content of the email we are sending.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Verification Code");
            // SecurityCode in this case is a random alphanumeric code of 5 figures.
            message.setText("Your verification code is: " + securityCode);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
