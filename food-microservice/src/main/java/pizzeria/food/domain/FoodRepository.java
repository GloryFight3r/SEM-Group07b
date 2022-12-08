package pizzeria.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findById(long id);
}
