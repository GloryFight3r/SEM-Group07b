package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Autowired
    public AllergenController(AllergenService allergenService) {
        this.allergenService = allergenService;
    }
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
