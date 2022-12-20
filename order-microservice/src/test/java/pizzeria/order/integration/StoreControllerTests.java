package pizzeria.order.integration;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.api.constraints.UpperChars;
import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.lifecycle.BeforeProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pizzeria.order.authentication.AuthManager;
import pizzeria.order.authentication.JwtTokenVerifier;
import pizzeria.order.domain.store.StoreRepository;
import pizzeria.order.integration.utils.JsonUtil;
import pizzeria.order.models.StoreModel;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class StoreControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient AuthManager mockAuthManager;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient StoreRepository storeRepo;

    @Test
    void createStore_worksCorrectly() throws Exception {
        StoreModel firstStore = new StoreModel();
        firstStore.setContact("borislav@gmail.com");
        firstStore.setLocation("NL-2624ME");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/store/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(firstStore)));

        resultActions.andExpect(status().isCreated());

        assertThat(storeRepo.findById(1).get().getContact()).isEqualTo(firstStore.getContact());
        assertThat(storeRepo.findById(1).get().getLocation()).isEqualTo(firstStore.getLocation());
    }

    @ParameterizedTest
    @MethodSource("incorrectEmailSuite")
    void createStore_incorrectMail(String email) throws Exception {
        StoreModel firstStore = new StoreModel();
        firstStore.setContact(email);
        firstStore.setLocation("NL-2624ME");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/store/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(firstStore)));

        resultActions.andExpect(status().isBadRequest());
    }

    static Stream<Arguments> incorrectEmailSuite() {
        return Stream.of(
                Arguments.of("borislav@gmail"),
                Arguments.of("@gmail.com"),
                Arguments.of("bbb@gmail."),
                Arguments.of("borislav@gmail."),
                Arguments.of("borislav@a.com"),
                Arguments.of("borislava.com")
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectLocationSuite")
    void createStore_incorrectLocation(String location) throws Exception {
        StoreModel firstStore = new StoreModel();
        firstStore.setContact("borislav@gmail.com");
        firstStore.setLocation(location);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/store/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(firstStore)));

        resultActions.andExpect(status().isBadRequest());
    }

    static Stream<Arguments> incorrectLocationSuite() {
        return Stream.of(
                Arguments.of("NL-262ME"),
                Arguments.of("Nl-2624ME"),
                Arguments.of("NL-2624Me"),
                Arguments.of("NL-ME"),
                Arguments.of("NL-99991ME"),
                Arguments.of("NL999ME"),
                Arguments.of("NL999M2"),
                Arguments.of("NL99932")
        );
    }

    @Test
    void editStore_worksCorrectly() {

    }

    @Test
    void deleteStore_worksCorrectly() {

    }

    @Test
    void getStores_worksCorrectly() {

    }
}
