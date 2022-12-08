package pizzeria.authentication.domain.user;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import pizzeria.authentication.domain.HasEvents;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    public static boolean containsRole(String role) {
        List<String> roles = List.of("ADMIN", "CUSTOMER", "MANAGER");
        return roles.contains(role);
    }

    @Column(name = "role", nullable = false)
    private String role;

    public String getRole() {
        return role;
    }

    /**
     * Create new application user.
     *
     * @param id The NetId for the new user
     * @param password The password for the new user
     * @param role The password for the new user
     */
    public AppUser(String id, HashedPassword password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.recordThat(new UserWasCreatedEvent(id));
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public String getId() {
        return id;
    }

    public HashedPassword getPassword() {
        return password;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return id.equals(appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
