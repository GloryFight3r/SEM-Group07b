package pizzeria.food.domain.Allergens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientRepository;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.domain.recipe.RecipeRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class AllergenService {
    private final transient RecipeRepository recipeRepository;
    private final transient IngredientRepository ingredientRepository;

    @Autowired
    public AllergenService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> filterMenuOnAllergens(List<String> allergens) throws IngredientNotFoundException {
        List<Recipe> recipes = recipeRepository.findAll();
        List<Recipe> menu = new ArrayList<>();
        for (Recipe recipe: recipes){
            if (recipeIsSafe(recipe, allergens)){
                menu.add(recipe);
            }
        }
        return menu;
    }

    public boolean recipeIsSafe(Recipe recipe, List<String> allergens) throws IngredientNotFoundException {
        List<Long> ids = recipe.getBaseToppings();
        for (long id: ids){
            if (ingredientRepository.existsById(id)){
                List<String> allergensOfIngredient = ingredientRepository.findById(id).get().getAllergens();
                for (String allergen: allergensOfIngredient){
                    if (allergens.contains(allergen)) return false;
                }
            } else {
                throw new IngredientNotFoundException();
            }
        }
        return true;
    }
}
