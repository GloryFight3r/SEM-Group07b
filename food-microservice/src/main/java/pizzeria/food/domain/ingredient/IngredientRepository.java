package pizzeria.food.domain.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM ingredient WHERE isBaseTopping = 0", nativeQuery = true)
    List<Ingredient> getDefaultToppings();
}
