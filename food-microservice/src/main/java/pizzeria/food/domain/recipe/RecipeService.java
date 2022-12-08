package pizzeria.food.domain.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
