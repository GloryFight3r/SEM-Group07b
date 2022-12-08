package pizzeria.food.domain.ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final transient IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient registerIngredient(Ingredient ingredient) throws IngredientAlreadyInUseException {
        if (ingredientRepository.existsById(ingredient.getId()) || ingredientRepository.existsByName(ingredient.getName())) {
            throw new IngredientAlreadyInUseException();
        }
        Ingredient result = ingredientRepository.save(ingredient);
        return result;
    }

    public Ingredient updateIngredient(Ingredient ingredient) throws IngredientNotFoundException {
        if (ingredientRepository.existsById(ingredient.getId())) {
            ingredientRepository.deleteById(ingredient.getId());
            return ingredientRepository.save(ingredient);
        } else {
            throw new IngredientNotFoundException();
        }
    }

    public boolean deleteIngredient(long id) throws IngredientNotFoundException {
        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
            return true;
        }
        throw new IngredientNotFoundException();
    }


}
