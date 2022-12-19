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

    /**
     * Constructor for the AllergenService class that auto wires the required databases
     * @param recipeRepository RecipeRepository in which we will perform all recipe related operations
     * @param ingredientRepository IngredientRepository in which we will perform all ingredient related operations
     */
    @Autowired
    public AllergenService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * @param allergens list of strings that represents the allergens we need to filter on
     * @return a list of recipe that does not contain any of the specified allergies
     * @throws IngredientNotFoundException when an ingredient of a recipe wasn't found in the database
     */
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

    /**
     * @param recipe Recipe we want to check for allergens
     * @param allergens list of strings that represents the allergens
     * @return true iff the recipe does not contain any of the allergens
     * @throws IngredientNotFoundException when an ingredient of this recipe is not stored in the database
     */
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
