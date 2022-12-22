package pizzeria.authentication.domain.user;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationServiceTests {

    @Autowired
    private transient RegistrationService registrationService;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;

    @Test
    public void createUser_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final String testUser = "SomeUser";
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        // Act
        registrationService.registerUser(testUser, testPassword);

        // Assert
        AppUser savedUser = userRepository.findById(testUser).orElseThrow();

        assertThat(savedUser.getId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
    }

    @Test
    void testRegisterMoreThan5Users() {
        HashedPassword hp = new HashedPassword("pass");
        Password p = new Password("pass");
        AppUser user1 = new AppUser("id1", hp);
        AppUser user2 = new AppUser("id2", hp);
        AppUser user3 = new AppUser("id3", hp);
        AppUser user4 = new AppUser("id4", hp);
        AppUser user5 = new AppUser("id5", hp);
        AppUser user6 = new AppUser("id6", hp);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        assertEquals(userRepository.count(), 5L);
        when(mockPasswordEncoder.hash(p)).thenReturn(hp);
        try {
            assertEquals(registrationService.registerUser("id6", p), user6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUser_withExistingUser_throwsException() {
        // Arrange
        final String testUser = "SomeUser";
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Password newTestPassword = new Password("password456");

        AppUser existingAppUser = new AppUser(testUser, existingTestPassword);

        userRepository.save(existingAppUser);

        // Act
        ThrowableAssert.ThrowingCallable action = () -> registrationService.registerUser(testUser, newTestPassword);

        // Assert
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(action);

        AppUser savedUser = userRepository.findById(testUser).orElseThrow();

        assertThat(savedUser.getId()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword);
    }
}
