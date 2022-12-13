package pizzeria.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pizzeria.user.authentication.AuthManager;
import pizzeria.user.communication.HttpRequestService;
import pizzeria.user.domain.user.User;
import pizzeria.user.domain.user.UserService;
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
    public ResponseEntity create(@RequestBody UserModel user) {
        try {
            userService.saveUser(user);

            Optional<User> savedUser = userService.findUserByEmail(user.getEmail());

            if (savedUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //registers the user in the authenticate-microservice database
            if (!httpRequestService.registerUser(savedUser.get(), user.getPassword())) {
                userService.deleteUser(savedUser.get().getEmail());
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * A method which returns all the users registered in our database
     * @return List of all the users in our database
     */
    @GetMapping("/get_users")
    public List<User> getUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /*@DeleteMapping("/delete_user")
    public ResponseEntity deleteUser(@RequestBody UserModel user) {
        try {
            boolean flag = userService.deleteUser(user.getEmail());
            if (flag) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }*/

    /**
     * Endpoint which allows changing of allergies for a given user. The id user is extracted from the JWT
     * token that is used for authentication
     * @param allergiesModel AllergiesModel which contains a list of the new allergies
     * @return A response indicating either failure or success
     */
    @PutMapping("/update_allergies")
    public ResponseEntity updateAllergies(@RequestBody AllergiesModel allergiesModel) {
        try {
            boolean flag = userService.updateUserAllergies(authManager.getNetId(), allergiesModel.getAllergies());
            if (flag) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
        try {
            List <String> allergies = userService.getAllergies(authManager.getNetId());
            if (allergies != null) {
                return ResponseEntity.ok().body(new AllergiesResponseModel(allergies));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
        try {
            Optional<User> user = userService.findUserByEmail(loginModel.getEmail());

            if (user.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Optional <String> jwtToken = httpRequestService.loginUser(user.get().getId(), loginModel.getPassword());
            if (jwtToken.isEmpty()) {
               return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().body(new LoginResponseModel(jwtToken.get()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
