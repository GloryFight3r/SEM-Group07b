package pizzeria.user.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.user.models.UserModel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTests {
    @Autowired
    private transient UserService userService;

    @Autowired
    private transient UserRepository userRepository;

    private UserModel userModel;
    private String email, name, password, role;
    private List<String> allergies;

    @BeforeEach
    public void init() {
        email = "Test1";
        name = "Test1";
        password = "coolpassword";
        role = "ROLE_MANAGER";
        allergies = List.of("Al1", "Al2", "Al3");


        userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setName(name);
        userModel.setPassword(password);
        userModel.setAllergies(allergies);
        userModel.setRole(role);
    }

    @Test
    public void saveUser_worksCorrectly() {
        userService.saveUser(userModel);

        Optional<User> actualUser = userRepository.findUserByEmail(email);

        assertThat(actualUser).isNotEmpty();

        assertThat(actualUser.get().getEmail()).isEqualTo(email);
        assertThat(actualUser.get().getName()).isEqualTo(name);
        assertThat(actualUser.get().getRole()).isEqualTo(role);
        assertThat(actualUser.get().getAllergies()).containsExactlyElementsOf(allergies);
    }

    @Test
    public void findUserByEmail_worksCorrectly() {
        User tempUser = new User(role, name, email, allergies);

        userRepository.save(tempUser);

        Optional<User> actualUser = userService.findUserByEmail(email);

        assertThat(actualUser).isNotEmpty();

        assertThat(actualUser.get()).isEqualTo(tempUser);
    }

    @Test
    public void getAllUsers_worksCorrectly() {
        User tempUser1 = new User(role, name, email, allergies);
        User tempUser2 = new User(role, name, "borislav2@gmail.com", allergies);
        User tempUser3 = new User(role, name, "borislav3@gmail.com", allergies);

        userRepository.save(tempUser1);
        userRepository.save(tempUser2);
        userRepository.save(tempUser3);

        List<User> getActualUsers = userService.getAllUsers();

        assertThat(List.of(tempUser1, tempUser2, tempUser3)).containsExactlyInAnyOrderElementsOf(getActualUsers);
    }

    @Test
    public void deleteUser_worksCorrectly() {
        userRepository.save(new User(role, name, email, allergies));

        assertThat(userRepository.findUserByEmail(email)).isNotEmpty();

        userService.deleteUser(email);

        assertThat(userRepository.findUserByEmail(email)).isEmpty();
    }

    @Test
    public void updateUserAllergies_worksCorrectly() {
        User tempUser = new User(role, name, email, allergies);
        List <String> newAllergies = List.of("newAllergy1", "newAllergy2", "newAllergy3");

        userRepository.save(tempUser);

        userService.updateUserAllergies(tempUser.getId(), newAllergies);

        Optional <User> updatedUser = userRepository.findUserById(tempUser.getId());

        assertThat(updatedUser).isNotEmpty();

        assertThat(updatedUser.get().getAllergies()).containsExactlyInAnyOrderElementsOf(newAllergies);
    }

    @Test
    public void getAllergies_worksCorrectly() {
        User tempUser = new User(role, name, email, allergies);

        userRepository.save(tempUser);

        Optional <User> actualUser = userRepository.findUserById(tempUser.getId());

        assertThat(actualUser).isNotEmpty();

        assertThat(actualUser.get().getAllergies()).containsExactlyInAnyOrderElementsOf(allergies);
    }
}
