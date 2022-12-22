package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.food.communication.HttpRequestService;
import pizzeria.food.domain.Allergens.AllergenService;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.domain.recipe.RecipeNotFoundException;
import pizzeria.food.models.allergens.CheckIfRecipeIsSafeRequestModel;
import pizzeria.food.models.allergens.FilterMenuResponseModel;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/allergens")
public class AllergenController {

    private final transient AllergenService allergenService;
    private final transient HttpRequestService requestService;

    /**
     * Constructor for the AllergenController class that auto wires the required service
     * @param allergenService AllergenService that handles the allergen complexity
     */
    @Autowired
    public AllergenController(AllergenService allergenService, HttpRequestService requestService) {
        this.allergenService = allergenService;
        this.requestService = requestService;
    }


    /**
     * @param token The token of the user
     * @return List of recipes that don't contain the specified allergens
     */
    @GetMapping("/menu")
    public ResponseEntity<FilterMenuResponseModel> filterMenu(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

            Optional<List<String>> allergens = requestService.getUserAllergens(token);
            if (allergens.isPresent()) {
                try {
                    List<Recipe> filteredMenu = allergenService.filterMenuOnAllergens(allergens.get());
                    FilterMenuResponseModel responseModel = new FilterMenuResponseModel();
                    responseModel.setRecipes(filteredMenu);
                    return ResponseEntity.status(HttpStatus.OK).body(responseModel);
                } catch (IngredientNotFoundException e) {
                    return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

    }

    @GetMapping("/warn")
    public ResponseEntity<Boolean> checkIfSafe(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CheckIfRecipeIsSafeRequestModel requestModel) {
        Optional<List<String>> allergens = requestService.getUserAllergens(token);
        if (allergens.isPresent()) {
            try {
                boolean isSafe = allergenService.checkIfSafeRecipeWithId(requestModel.getId(), allergens.get());
                return ResponseEntity.status(HttpStatus.OK).body(isSafe);
            } catch (RecipeNotFoundException e) {
                return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
            } catch (IngredientNotFoundException e) {
                return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
