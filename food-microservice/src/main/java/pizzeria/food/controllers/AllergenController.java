package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.food.domain.Allergens.AllergenService;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.models.allergens.FilterMenuRequestModel;
import pizzeria.food.models.allergens.FilterMenuResponseModel;

import java.util.List;

@RestController
@RequestMapping("/allergens")
public class AllergenController {

    private final transient AllergenService allergenService;

    /**
     * Constructor for the AllergenController class that auto wires the required service
     * @param allergenService AllergenService that handles the allergen complexity
     */
    @Autowired
    public AllergenController(AllergenService allergenService){
        this.allergenService = allergenService;
    }


    /**
     * @param requestModel FilterMenuRequestModel that holds the allergens of the user
     * @return List of recipes that don't contain the specified allergens
     */
    @GetMapping("/menu")
    public ResponseEntity<FilterMenuResponseModel> filterMenu(@RequestBody FilterMenuRequestModel requestModel){
        try {
            List<Recipe> filteredMenu = allergenService.filterMenuOnAllergens(requestModel.getAllergens());
            FilterMenuResponseModel responseModel = new FilterMenuResponseModel();
            responseModel.setRecipes(filteredMenu);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }


}
