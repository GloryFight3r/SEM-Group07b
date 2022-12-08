package pizzeria.user.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.user.models.UserModel;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final transient UserRepository userRepository;
    @Autowired

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserModel user) {
        userRepository.save(user.parseToUser());
    }

    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findUserByEmail(mail);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(String mail) {
        return userRepository.deleteUserByEmail(mail) != 0;
    }

    public boolean updateUserAllergies(UserModel user) {
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());

        if (optionalUser.isEmpty()) {
            return false;
        }

        User currentUser = optionalUser.get();

        currentUser.setAllergies(user.getAllergies());

        userRepository.save(currentUser);

        return true;
    }
}
