package pizzeria.order.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.order.domain.store.Store;
import pizzeria.order.domain.store.StoreRepository;
import pizzeria.order.domain.store.StoreService;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StoreServiceTests {

    @Autowired
    private transient StoreService storeService;

    @Autowired
    private transient StoreRepository storeRepository;

    @Test
    void addStore_worksCorrectly() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        Optional<Store> actualStore = storeRepository.findById(newstore.getId());

        assertThat(actualStore.isPresent()).isTrue();

        assertThat(actualStore.get().getLocation()).isEqualTo(newstore.getLocation());
        assertThat(actualStore.get().getContact()).isEqualTo(newstore.getContact());
    }

    @ParameterizedTest
    @MethodSource("invalidArgumentsSuite")
    void addStore_invalidArguments(Store store, Class exception) throws Exception{
        assertThatThrownBy(() -> {
            storeService.addStore(store);
        }).isInstanceOf(exception);

        assertThat(storeRepository.existsById(1L)).isFalse();
    }

    static Stream<Arguments> invalidArgumentsSuite() {
        final String correctEmail = "borislavsemerdzhiev.02@gmail.com";
        final String correctLocation = "NL-2624ME";
        return Stream.of(
                Arguments.of(null, StoreService.StoreAlreadyExistException.class),
                Arguments.of(new Store("Nl-2624ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "@gmail.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "bbb@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@a.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislava.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store("NL-262ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("Nl-2624ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-2624Me", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-99991ME",correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL999ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL999M2", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL99932", correctEmail), StoreService.InvalidLocationException.class)
        );
    }

    @Test
    void editStore_worksCorrectly() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        Store editedStore = new Store("NL-2020ME", "newborislav.02@gmail.com");

        storeService.editStore(1L, editedStore);

        Optional<Store> actualStore = storeRepository.findById(1L);

        assertThat(actualStore.isPresent()).isTrue();
        assertThat(actualStore.get().getContact()).isEqualTo(editedStore.getContact());
        assertThat(actualStore.get().getLocation()).isEqualTo(editedStore.getLocation());
    }

    @Test
    void editStore_noSuchId() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        Store editedStore = new Store("NL-2020ME", "newborislav.02@gmail.com");

        assertThatThrownBy(() -> {
            storeService.editStore(2L, editedStore);
        }).isInstanceOf(StoreService.StoreDoesNotExistException.class);

        Optional<Store> actualStore = storeRepository.findById(1L);

        assertThat(actualStore.isPresent()).isTrue();
        assertThat(actualStore.get().getContact()).isEqualTo(newstore.getContact());
        assertThat(actualStore.get().getLocation()).isEqualTo(newstore.getLocation());
    }

    @ParameterizedTest
    @MethodSource("editInvalidArgumentsSuite")
    void editStore_invalidArguments(Store editStore, Class exception) throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        assertThatThrownBy(() -> {
            storeService.editStore(1L, editStore);
        }).isInstanceOf(exception);

        Optional<Store> actualStore = storeRepository.findById(1L);

        assertThat(actualStore.isPresent()).isTrue();
        assertThat(actualStore.get().getContact()).isEqualTo(newstore.getContact());
        assertThat(actualStore.get().getLocation()).isEqualTo(newstore.getLocation());
    }

    static Stream<Arguments> editInvalidArgumentsSuite() {
        final String correctEmail = "borislavsemerdzhiev.02@gmail.com";
        final String correctLocation = "NL-2624ME";
        return Stream.of(
                Arguments.of(new Store("Nl-2624ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "@gmail.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "bbb@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@gmail."), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislav@a.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store(correctLocation, "borislava.com"), StoreService.InvalidEmailException.class),
                Arguments.of(new Store("NL-262ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("Nl-2624ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-2624Me", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL-99991ME",correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL999ME", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL999M2", correctEmail), StoreService.InvalidLocationException.class),
                Arguments.of(new Store("NL99932", correctEmail), StoreService.InvalidLocationException.class)
        );
    }

    @Test
    void deleteStore_deletesCorrectly() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        storeService.deleteStore(1L);

        assertThat(storeRepository.existsById(1L)).isFalse();
    }

    @Test
    void deleteStore_noSuchStore() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        assertThatThrownBy(() -> {
            storeService.deleteStore(2L);
        }).isInstanceOf(StoreService.StoreDoesNotExistException.class);

        assertThat(storeRepository.existsById(1L)).isTrue();
    }

    @Test
    void deleteStore_alreadyDeleted() throws Exception {
        Store newstore = new Store("NL-2624ME", "borislavsemerdzhiev.02@gmail.com");
        storeService.addStore(newstore);

        storeService.deleteStore(1L);

        assertThatThrownBy(() -> {
            storeService.deleteStore(2L);
        }).isInstanceOf(StoreService.StoreDoesNotExistException.class);

        assertThat(storeRepository.existsById(1L)).isFalse();
    }

    @Test
    void getEmailById() {
        
    }

    @Test
    void existsById() {
    }

    @Test
    void getAllStores() {
    }
}
