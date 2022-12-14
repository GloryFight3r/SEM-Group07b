package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.recipe.RecipeNotFoundException;
import pizzeria.food.domain.recipe.RecipeService;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.domain.recipe.RecipeAlreadyInUseException;
import pizzeria.food.models.recipe.*;

import java.util.List;


@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final transient RecipeService foodService;

    /**
     * Constructor for the RecipeController class that auto wires the required service
     * @param foodService RecipeService that handles all the Recipe complexity
     */
    @Autowired
    public RecipeController(RecipeService foodService){
        this.foodService = foodService;
    }

    /**
     * @param model SaveFoodRequestModel that holds the Recipe we want to save
     * @return SaveFoodResponseModel that holds the saved recipe
     */
    @PostMapping("/save")
    public ResponseEntity<SaveFoodResponseModel> saveFood(@RequestBody SaveFoodRequestModel model){

        try {
            Recipe saved = foodService.registerFood(model.getRecipe());
            SaveFoodResponseModel responseModel = new SaveFoodResponseModel();
            responseModel.setId(saved.getId());
            responseModel.setRecipe(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (RecipeAlreadyInUseException | IngredientNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    /**
     * @param model UpdateFoodRequestModel holding the id and the recipe we want to update
     * @return Recipe that was updated
     */
    @PostMapping("/update")
    public ResponseEntity<UpdateFoodResponseModel> updateFood(@RequestBody UpdateFoodRequestModel model) {
        try {
            Recipe updated = foodService.updateFood(model.getRecipe(), model.getId());
            UpdateFoodResponseModel responseModel = new UpdateFoodResponseModel();
            responseModel.setId(updated.getId());
            responseModel.setRecipe(updated);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (RecipeNotFoundException | IngredientNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    /**
     * @param model DeleteFoodRequestModel that holds the id of the Recipe we want to delete
     */
    @DeleteMapping("/delete")
    public ResponseEntity deleteFood(@RequestBody DeleteFoodRequestModel model) {
        try {
            foodService.deleteFood(model.getId());
            return ResponseEntity.ok().build();
        } catch (RecipeNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    /**
     * @return MenuResponseModel holding the list of available recipes
     */
    @GetMapping("/menu")
    public ResponseEntity<MenuResponseModel> getMenu() {
        List<Recipe> menu = foodService.getMenu();
        MenuResponseModel responseModel = new MenuResponseModel();
        responseModel.setMenu(menu);
        return ResponseEntity.ok().body(responseModel);
    }

}
