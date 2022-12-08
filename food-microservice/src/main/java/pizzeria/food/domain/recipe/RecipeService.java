package pizzeria.food.domain.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private final transient RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository){
        this.recipeRepository = recipeRepository;
    }

    public Recipe registerFood(Recipe recipe) throws RecipeAlreadyInUseException {
        if (recipeRepository.existsById(recipe.getId()) || recipeRepository.existsByName(recipe.getName())) {
            throw new RecipeAlreadyInUseException();
        }
        Recipe result = recipeRepository.save(recipe);
        return result;
    }

    public Recipe updateFood(Recipe recipe) throws RecipeNotFoundException {
        if (recipeRepository.existsById(recipe.getId())) {
            recipeRepository.deleteById(recipe.getId());
            return recipeRepository.save(recipe);
        }
        throw new RecipeNotFoundException();
    }

    public boolean deleteFood(Recipe recipe) throws RecipeNotFoundException {
        if (recipeRepository.existsById(recipe.getId())) {
            recipeRepository.deleteById(recipe.getId());
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
}
