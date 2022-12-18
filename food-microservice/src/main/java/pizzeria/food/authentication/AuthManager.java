package pizzeria.food.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {
    //TODO: authentication management for placing, deleting, editing orders
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
