package pizzeria.user.domain.user;

import pizzeria.user.models.UserModel;

/**
 * Exception to indicate the NetID is already in use.
 */
public class InvalidUserArgumentsException extends Exception {
    static final long serialVersionUID = -4387516993124229949L;

    public InvalidUserArgumentsException(UserModel user) {
        super(user.toString());
    }
}
