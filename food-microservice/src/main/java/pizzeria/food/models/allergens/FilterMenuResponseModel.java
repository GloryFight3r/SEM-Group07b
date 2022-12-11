package pizzeria.food.models.allergens;

import lombok.Data;
import pizzeria.food.domain.recipe.Recipe;

import java.util.List;

@Data
public class FilterMenuResponseModel {
    private List<Recipe> recipes;
}
