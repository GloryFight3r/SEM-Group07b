package pizzeria.food.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {

    public String getToken() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }


}
