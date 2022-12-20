package pizzeria.food.domain.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientRepository;
import pizzeria.food.models.prices.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {
    private final transient RecipeRepository recipeRepository;
    private final transient IngredientRepository ingredientRepository;

    /**
     * Constructor for the RecipeService class that auto wires the required databases.
     * @param recipeRepository RecipeRepository in which we will perform all recipe related operations
     * @param ingredientRepository ingredientRepository that we use to check existence of ingredients
     */
    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository){
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * @param recipe The Recipe instance we want to store in the database.
     * @return Recipe instance that was saved in the database with the corresponding id.
     * @throws RecipeAlreadyInUseException thrown when we try to save a recipe that already exists by id or name.
     * @throws IngredientNotFoundException thrown when we try to save a recipe that has ingredients that aren't stored
     * in the database.
     */
    public Recipe registerFood(Recipe recipe) throws RecipeAlreadyInUseException, IngredientNotFoundException {
        if (recipeRepository.existsById(recipe.getId()) || recipeRepository.existsByName(recipe.getName())) {
            throw new RecipeAlreadyInUseException();
        }
        for (long id: recipe.getBaseToppings()){
            if (!ingredientRepository.existsById(id)){
                throw new IngredientNotFoundException();
            }
        }
        Recipe result = recipeRepository.save(recipe);
        return result;
    }

    /**
     * @param recipe Recipe instance that carries the data that we want to store instead of the recipe
     *               that is currently in the database.
     * @param id long value representing the id of the recipe we want to update.
     * @return the Recipe that is saved in the database if we are able to update it
     * @throws RecipeNotFoundException thrown when the given id is not associated with a recipe in the
     * database.
     * @throws IngredientNotFoundException thrown when one of the ingredient ids in the recipe is not stored in the
     * database.
     */
    public Recipe updateFood(Recipe recipe, long id) throws RecipeNotFoundException, IngredientNotFoundException {
        for (long idIngredients: recipe.getBaseToppings()){
            if (!ingredientRepository.existsById(idIngredients)){
                throw new IngredientNotFoundException();
            }
        }
        if (recipeRepository.existsById(id)) {
            recipe.setId(id);
            return recipeRepository.save(recipe);
        }
        throw new RecipeNotFoundException();
    }

    /**
     * @param id long value representing the id of the recipe we want to delete.
     * @return true iff the recipe was deleted successfully.
     * @throws RecipeNotFoundException thrown when the id is not associated to a recipe
     * in the database.
     */
    public boolean deleteFood(long id) throws RecipeNotFoundException {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        throw new RecipeNotFoundException();
    }

    /**
     * @param ids List longs representing the ids of the recipes of which we want to get the prices
     * @return a list of doubles that are the prices of the recipes
     * @throws RecipeNotFoundException thrown when one of the ids of the recipes was not in the database.
     */
    @SuppressWarnings("PMD")
    public Map<Long, Tuple> getPrices(List<Long> ids) throws RecipeNotFoundException {
        Map<Long, Tuple> prices = new HashMap<>(ids.size());
        for (long id: ids){
            if (recipeRepository.existsById(id)) {
                Recipe recipe = recipeRepository.findById(id).get();
                prices.put(id, new Tuple(recipe.getBasePrice(), recipe.getName()));
            } else {
                throw new RecipeNotFoundException("The Recipe with the id " + id + " was not found in the databases");
            }
        }
        return prices;
    }

    /**
     * @return List of recipes that represents the menu
     */
    public List<Recipe> getMenu(){
        return recipeRepository.findAll();
    }
}
