package pizzeria.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pizzeria.user.domain.user.User;
import pizzeria.user.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import pizzeria.user.models.UserModel;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final transient UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create_user")
    public ResponseEntity create(@RequestBody UserModel user) {
        try {
            //System.out.println("User created" + " " + user.toString());
            userService.saveUser(user);

            //registers the user in the authenticate-microservice database
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
