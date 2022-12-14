package pizzeria.food.domain.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private final transient RecipeRepository recipeRepository;
    private final transient IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository){
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

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

    public Recipe updateFood(Recipe recipe, long id) throws RecipeNotFoundException, IngredientNotFoundException {
        if (recipeRepository.existsById(id)) {
            recipe.setId(id);
            return recipeRepository.save(recipe);
        }
        for (long idIngredients: recipe.getBaseToppings()){
            if (!ingredientRepository.existsById(idIngredients)){
                throw new IngredientNotFoundException();
            }
        }
        throw new RecipeNotFoundException();
    }

    public boolean deleteFood(long id) throws RecipeNotFoundException {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        throw new RecipeNotFoundException();
    }
    @SuppressWarnings("PMD")
    public List<Double> getPrices(List<Long> ids) throws RecipeNotFoundException {
        List<Double> prices = new ArrayList<>(ids.size());
        for (long id: ids){
            if (recipeRepository.existsById(id)) {
                Recipe recipe = recipeRepository.findById(id).get();
                prices.add(recipe.getBasePrice());
            } else {
                throw new RecipeNotFoundException("The Recipe with the id " + id + " was not found in the databases");
            }
        }
        return prices;
    }

    public List<Recipe> getMenu(){
        return recipeRepository.findAll();
    }
}
