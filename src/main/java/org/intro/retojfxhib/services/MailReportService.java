package org.intro.retojfxhib.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class MailReportService implements Runnable {
    private String userName = "mymovieshelf615@gmail.com";
    private String password = System.getenv("MAIL_PASSWORD");
    private File   fileToSend;
    private String userEmail;

    public MailReportService(String userEmail, File pdfFile) {
        this.userEmail = userEmail;
        this.fileToSend = pdfFile;
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
            message.setSubject("Pdf MyMovieShelf Report");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Thanks for using MyMovieShelf, we've already sent you the pdf requested enjoy it.");

            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile(fileToSend);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(pdfAttachment);

            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}