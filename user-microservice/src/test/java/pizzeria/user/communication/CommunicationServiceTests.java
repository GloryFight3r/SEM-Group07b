package pizzeria.user.communication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import pizzeria.user.domain.user.User;
import pizzeria.user.models.AuthenticationResponseModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockRestTemplate", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CommunicationServiceTests {
    @Autowired
    private transient RestTemplate restTemplate;

    @Test
    public void registerUser_worksCorrectly() {
        when(restTemplate.postForEntity(eq("http://localhost:8081/register"), any(), eq(ResponseEntity.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        HttpRequestService httpRequestService = new HttpRequestService(restTemplate);

        boolean flag = httpRequestService.registerUser(new User("Borislav", "Borislav@gmail.com", List.of("Allergy1")), "Password1");

        assertThat(flag).isEqualTo(true);
    }

    @Test
    public void loginUser_worksCorrectly() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzd" +
                "WIiOiI5Zjg2NjE5OS01ZDcwLTQ2MWEtOGY5OC0xZmIzOTM3YzNjNTYiLCJyb2xlIjoiW1" +
                "JPTEVfTUFOQUdFUl0iLCJleHAiOjE2NzEwMjMxNjcsImlhdCI6MTY3MDkzNjc2N30.iT7k5l" +
                "t03EqOfCUG-V_zjE_c_USNcvG_7nhWu6gycQSuHMUWB__fnSSVJVKRms2vD8-PeMX344DJ_aUqRxIPDA";

        when(restTemplate.postForEntity(eq("http://localhost:8081/authenticate"), any(), eq(AuthenticationResponseModel.class)))
                .thenReturn(ResponseEntity.ok().body(new AuthenticationResponseModel(token)));

        HttpRequestService httpRequestService = new HttpRequestService(restTemplate);

        Optional <String> actualToken = httpRequestService.loginUser("mockedId", "Password1");

        assertThat(actualToken.get()).isEqualTo(token);
    }
}
