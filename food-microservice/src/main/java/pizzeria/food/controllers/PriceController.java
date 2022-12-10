package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientService;
import pizzeria.food.domain.recipe.RecipeNotFoundException;
import pizzeria.food.domain.recipe.RecipeService;
import pizzeria.food.models.prices.GetPricesRequestModel;
import pizzeria.food.models.prices.GetPricesResponseModel;

import java.util.List;

@RestController
@RequestMapping("/price")
public class PriceController {
    private final transient RecipeService recipeService;
    private final transient IngredientService ingredientService;

    @Autowired
    public PriceController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ids")
    public ResponseEntity<GetPricesResponseModel> getPrices(@RequestBody GetPricesRequestModel requestModel) {
        try {
            List<Double> foodPrices = recipeService.getPrices(requestModel.getFoodIds());
            List<Double> ingredientPrices = ingredientService.getPrices(requestModel.getIngredientIds());
            GetPricesResponseModel responseModel = new GetPricesResponseModel();
            responseModel.setFoodPrices(foodPrices);
            responseModel.setIngredientPrices(ingredientPrices);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (RecipeNotFoundException | IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }

    }
}
