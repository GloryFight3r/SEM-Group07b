package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.food.domain.recipe.RecipeNotFoundException;
import pizzeria.food.domain.recipe.RecipeService;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.domain.recipe.RecipeAlreadyInUseException;
import pizzeria.food.models.recipe.*;


@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final transient RecipeService foodService;

    @Autowired
    public RecipeController(RecipeService foodService){
        this.foodService = foodService;
    }

    @PostMapping("/save")
    public ResponseEntity<SaveFoodResponseModel> saveFood(@RequestBody SaveFoodRequestModel model){

        try {
            Recipe saved = foodService.registerFood(model.getRecipe());
            SaveFoodResponseModel responseModel = new SaveFoodResponseModel();
            responseModel.setId(saved.getId());
            responseModel.setRecipe(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (RecipeAlreadyInUseException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateFoodResponseModel> updateFood(@RequestBody UpdateFoodRequestModel model) {
        try {
            Recipe updated = foodService.updateFood(model.getRecipe(), model.getId());
            UpdateFoodResponseModel responseModel = new UpdateFoodResponseModel();
            responseModel.setId(updated.getId());
            responseModel.setRecipe(updated);
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        } catch (RecipeNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteFood(@RequestBody DeleteFoodRequestModel model) {
        try {
            foodService.deleteFood(model.getId());
            return ResponseEntity.ok().build();
        } catch (RecipeNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }




}
