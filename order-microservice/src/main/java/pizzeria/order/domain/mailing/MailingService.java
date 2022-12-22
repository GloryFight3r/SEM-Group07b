package pizzeria.order.domain.mailing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.order.Application;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailingService {
    // The email we use to send messages from
    final transient String fromEmail = "fivenightsatandys7b@gmail.com";
    // The authentication password we use
    final transient String fromPassword = "ycgmcwdcuopnrbrd";

    final transient MessageTransport messageTransport;

    public enum ProcessType {
        CREATED,
        EDITED,
        DELETED
    }

    transient private Session session;

    @Autowired
    public MailingService(MessageTransport messageTransport) {
        this.messageTransport = messageTransport;

        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Application.ExcludeFromJacocoGeneratedReport
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });

        // Used to debug SMTP issues
        session.setDebug(true);
    }

    /**
     * Notify the store about the creation/edit/deletion of an order with the current orderId
     * @param orderId ID of the order
     * @param recipientEmail Email of the store
     * @param processType Type of the process CREATED/EDITED/DELETED
     */
    @SuppressWarnings("PMD")
    public void sendEmail(Long orderId, String recipientEmail, ProcessType processType) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set Subject: header field
            switch (processType) {
                case EDITED:
                    message.setSubject("Order has been edited!");
                    // Now set the actual message
                    message.setText(String.format("Order with orderId : %d has been edited", orderId));
                    break;
                case CREATED:
                    message.setSubject("Order has been created!");
                    // Now set the actual message
                    message.setText(String.format("Order with orderId : %d has been created", orderId));
                    break;
                case DELETED:
                    message.setSubject("Order has been deleted!");
                    // Now set the actual message
                    message.setText(String.format("Order with orderId : %d has been deleted", orderId));
                    break;
            }
            // Send message
            messageTransport.sendMessage(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
