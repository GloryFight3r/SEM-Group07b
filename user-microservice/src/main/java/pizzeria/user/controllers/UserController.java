package pizzeria.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pizzeria.user.communication.HttpRequestService;
import pizzeria.user.domain.user.User;
import pizzeria.user.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pizzeria.user.models.UserModel;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final transient UserService userService;

    private final transient HttpRequestService httpRequestService;

    @Autowired
    public UserController(UserService userService, HttpRequestService httpRequestService) {
        this.userService = userService;
        this.httpRequestService = httpRequestService;
    }

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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_users")
    public List<User> getUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete_user")
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
    }

    @PutMapping("/update_allergies")
    public ResponseEntity updateAllergies(@RequestBody UserModel user) {
        try {
            boolean flag = userService.updateUserAllergies(user);
            if (flag) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
