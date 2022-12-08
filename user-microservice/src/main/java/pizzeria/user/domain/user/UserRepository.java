package pizzeria.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A DDD repository for quering and persisting user aggregate roots.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Find user by ID.
     */

    List<User> findAll();

    Optional<User> findById(Integer id);
}
