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

    public boolean updateUserAllergies(String id, List<String> allergies) {
        Optional<User> optionalUser = userRepository.findUserById(id);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User currentUser = optionalUser.get();

        currentUser.setAllergies(allergies);

        userRepository.save(currentUser);

        return true;
    }

    public List<String> getAllergies(String id) {
        Optional<User> optionalUser = userRepository.findUserById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User currentUser = optionalUser.get();

        return currentUser.getAllergies();
    }
}
