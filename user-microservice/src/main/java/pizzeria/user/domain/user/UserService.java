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

    /**
     * Save a user given UserModel containing the necessary information
     *
     * @param user UserModel containing information about the user
     */
    public void saveUser(UserModel user) throws EmailAlreadyInUseException{
        User userToSave = user.parseToUser();

        if (checkUniqueEmail(userToSave.getEmail())) {
            userRepository.save(userToSave);
        } else {
            throw new EmailAlreadyInUseException(user.getEmail());
        }
    }

    private boolean checkUniqueEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Finds the user given his unique email
     *
     * @param mail the user's unique email
     * @return Optional that contains the user in the case he exists in the database
     */
    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findUserByEmail(mail);
    }

    /**
     * Returns all the users in our database
     *
     * @return List containing all the users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user from the database, given his id
     *
     * @param id Unique id of the user
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Updates the allergies associated with the user with the given id
     *
     * @param id ID of the user
     * @param allergies The new allergies of the user
     * @return True or False depending on whether the user was found
     */
    public void updateUserAllergies(String id, List<String> allergies) {
        Optional<User> optionalUser = userRepository.findUserById(id);

        User currentUser = optionalUser.get();

        currentUser.setAllergies(allergies);

        userRepository.save(currentUser);
    }

    /**
     * Returns all the allergies associated with the given user
     *
     * @param id ID of the user
     * @return List of all the allergies of the current user
     */
    public List<String> getAllergies(String id) {
        Optional<User> optionalUser = userRepository.findUserById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User currentUser = optionalUser.get();

        return currentUser.getAllergies();
    }

    /**
     * Checks whether a user exists given his id
     * @param id unique id of the user
     * @return True or False depending on whether the user exists
     */
    public boolean userExistsById(String id) {
        return userRepository.existsById(id);
    }
}
