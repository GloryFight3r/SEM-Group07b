package pizzeria.food.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {

        /**
        * Get the current user.
        *
        * @return The current user.
        */
        public String getCurrentUser() {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }


}
