package pizzeria.order.domain.mailing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.mail.MessagingException;
import java.util.stream.Stream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockMessageTransport"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MailingServiceTests {
    @Autowired
    private MessageTransport messageTransport;

    @Autowired
    private MailingService mailingService;

    @ParameterizedTest
    @MethodSource("sendMessageSuite")
    public void sendMessage(MailingService.ProcessType type) throws Exception{
        doNothing().when(messageTransport).sendMessage(any());

        mailingService.sendEmail(1L, "tomsfighter@gmail.com", type);

        verify(messageTransport, times(1)).sendMessage(any());
    }

    static Stream<Arguments> sendMessageSuite() {
        return Stream.of(
          Arguments.of(MailingService.ProcessType.CREATED),
          Arguments.of(MailingService.ProcessType.DELETED),
          Arguments.of(MailingService.ProcessType.EDITED)
        );
    }

    @Test
    public void throwsError() throws Exception{
        doThrow(new MessagingException()).when(messageTransport).sendMessage(any());

        mailingService.sendEmail(1L, "tomsfighter@gmail.com", MailingService.ProcessType.CREATED);

        verify(messageTransport, times(1)).sendMessage(any());
    }
}
