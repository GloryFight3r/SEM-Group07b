package pizzeria.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll();
    @Transactional
    Long deleteUserByEmail(String netId);

    Optional<User> findUserByEmail(String mail);
}
