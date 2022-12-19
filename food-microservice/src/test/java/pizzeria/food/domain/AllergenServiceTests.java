package pizzeria.food.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.food.domain.Allergens.AllergenService;
import pizzeria.food.domain.ingredient.Ingredient;
import pizzeria.food.domain.ingredient.IngredientRepository;
import pizzeria.food.domain.recipe.Recipe;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AllergenServiceTests {
    @Autowired
    private transient IngredientRepository ingredientRepository;

    @Autowired
    private transient AllergenService allergenService;

    @Test
    void filterMenuOnAllergens() {
    }

    @Test
    void recipeIsSafe() {
        ingredientRepository.save(new Ingredient("Ingredient1", 10, List.of("Al1", "Al2")));
        ingredientRepository.save(new Ingredient("Ingredient2", 10, List.of("Al3", "Al4")));
        ingredientRepository.save(new Ingredient("Ingredient3", 10, List.of("Al7", "Al5")));

        Recipe recipe1 = new Recipe("recipe1", List.of(1L, 2L, 3L), 30.0);

        try {
            assertThat(allergenService.recipeIsSafe(recipe1, List.of("Al10", "Al11"))).isTrue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void recipeIsNotSafe() {
        ingredientRepository.save(new Ingredient("Ingredient1", 10, List.of("Al1", "Al2")));
        ingredientRepository.save(new Ingredient("Ingredient2", 10, List.of("Al3", "Al4")));
        ingredientRepository.save(new Ingredient("Ingredient3", 10, List.of("Al7", "Al5")));

        Recipe recipe1 = new Recipe("recipe1", List.of(1L, 2L, 3L), 30.0);

        try {
            assertThat(allergenService.recipeIsSafe(recipe1, List.of("Al10", "Al7"))).isFalse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
