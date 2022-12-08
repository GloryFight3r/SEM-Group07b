package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzeria.food.domain.recipe.RecipeNotFoundException;
import pizzeria.food.domain.recipe.RecipeService;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.domain.recipe.RecipeAlreadyInUseException;


@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final transient RecipeService foodService;

    @Autowired
    public RecipeController(RecipeService foodService){
        this.foodService = foodService;
    }

    @PostMapping("/save")
    public ResponseEntity<Recipe> saveFood(@RequestBody Recipe recipe){

        try {
            Recipe saved = foodService.registerFood(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RecipeAlreadyInUseException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Recipe> updateFood(@RequestBody Recipe recipe) {
        try {
            Recipe updated = foodService.updateFood(recipe);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (RecipeNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteFood(@RequestBody Recipe recipe) {
        try {
            foodService.deleteFood(recipe);
            return ResponseEntity.ok().build();
        } catch (RecipeNotFoundException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }


}
