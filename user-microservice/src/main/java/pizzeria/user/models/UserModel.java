package pizzeria.user.models;

import lombok.Data;
import pizzeria.user.domain.user.User;

import java.util.List;

@Data
public class UserModel {
    String email;
    List<String> allergies;
    String name;
    String role;

    public User parseToUser() {
        return new User(role, name, email, allergies);
    }
}
