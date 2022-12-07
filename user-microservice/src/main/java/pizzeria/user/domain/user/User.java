package pizzeria.user.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends HasEvents {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;

    @Column(name = "role", nullable = false)
    @Getter
    private String role;
    @Column(name = "name", nullable = false)
    @Getter
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    @Getter
    private String email;
    @Column(name = "allergies", nullable = false)
    @ElementCollection
    @Getter
    private List<String> allergies;

    public User(String role, String name, String email, List<String> allergies) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.allergies = allergies;
        //this.recordThat(new UserWasCreatedEvent(netId));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, name, email, allergies);
    }
}
