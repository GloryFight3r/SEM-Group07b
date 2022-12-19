package pizzeria.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pizzeria.user.authentication.AuthManager;
import pizzeria.user.communication.HttpRequestService;
import pizzeria.user.domain.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pizzeria.user.models.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final transient UserService userService;

    private final transient HttpRequestService httpRequestService;

    private final transient AuthManager authManager;

    /**
     * Dependency injection
     * @param userService User service
     * @param httpRequestService Http service used to send http requests
     * @param authManager Authentication manager from which we can get information about the current http request user
     */
    @Autowired
    public UserController(UserService userService, HttpRequestService httpRequestService, AuthManager authManager) {
        this.userService = userService;
        this.httpRequestService = httpRequestService;
        this.authManager = authManager;
    }

    /**
     * Endpoint used for creating new users
     * @param user UserModel which contains the following fields [email, role, allergies, name, password]
     * @return A response indicating either failure or success
     */
    @PostMapping("/create_user")
    public ResponseEntity create(@RequestBody UserRegisterModel user) {
        // perform UserModel data validation
        if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Arguments for user are " +
                    "invalid", new InvalidUserArgumentsException(user));
        }

        try {
            userService.saveUser(user);

            Optional<User> savedUser = userService.findUserByEmail(user.getEmail());

            if (savedUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not store the user in the database");
            }

            //registers the user in the authenticate-microservice database
            if (!httpRequestService.registerUser(savedUser.get(), user.getPassword())) {
                userService.deleteUser(savedUser.get().getEmail());

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not communicate with " +
                        "authentication service");
            }

        } catch (EmailAlreadyInUseException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with the given email " +
                    "has already been registered", e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    @GetMapping("/get_users")
    public List<User> getUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }*/

    /**
     * A method that deletes a user from the repository if the user has provided a valid JWT
     * @return Response indicating whether the user account has been deleted
     */
    @DeleteMapping("/delete_user")
    public ResponseEntity deleteUser() {
        if (userService.userExistsById(authManager.getNetId())) {
            userService.deleteUser(authManager.getNetId());
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with such id found");
        }
    }

    /**
     * Endpoint which allows changing of allergies for a given user. The id user is extracted from the JWT
     * token that is used for authentication
     * @param allergiesModel AllergiesModel which contains a list of the new allergies
     * @return A response indicating either failure or success
     */
    @PutMapping("/update_allergies")
    public ResponseEntity updateAllergies(@RequestBody AllergiesModel allergiesModel) {
        if (userService.userExistsById(authManager.getNetId())) {
            userService.updateUserAllergies(authManager.getNetId(), allergiesModel.getAllergies());
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such id not found");
        }
    }

    /**
     * Endpoint which returns all the allergies associated with a user in our database.
     * The user id for which we want the allergies is extracted from the JWT token
     * used for authentication
     *
     * @return A response indicating either failure or success and a list with allergies in the body
     */
    @GetMapping("/get_allergies")
    public ResponseEntity<AllergiesResponseModel> getAllergies() {
        if (userService.userExistsById(authManager.getNetId())) {
            List <String> allergies = userService.getAllergies(authManager.getNetId());

            return ResponseEntity.ok().body(new AllergiesResponseModel(allergies));
        } else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such id not found");
        }
    }


    /**
     * Returns the jwt token associated with the user with the given email address,
     * provided we have the correct password
     *
     * @param loginModel Email and password for the user
     * @return If we successfully authenticate, we get the correct jwt token
     */
    @GetMapping("/login")
    public ResponseEntity<LoginResponseModel> loginUser(@RequestBody LoginModel loginModel) {
        if (loginModel.getEmail().isEmpty() || loginModel.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Login details' format is " +
                    "invalid", new InvalidLoginArgumentsException(loginModel));
        }

        Optional<User> user = userService.findUserByEmail(loginModel.getEmail());

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such email not found");
        }

        Optional <String> jwtToken = httpRequestService.loginUser(user.get().getId(), loginModel.getPassword());
        if (jwtToken.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not authenticate");
        }
        return ResponseEntity.ok().body(new LoginResponseModel(jwtToken.get()));
    }
}
