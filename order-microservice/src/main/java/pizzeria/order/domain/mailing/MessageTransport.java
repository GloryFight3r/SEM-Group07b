package pizzeria.order.domain.mailing;

import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@Component
public class MessageTransport {
    public void sendMessage(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }
}
