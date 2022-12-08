package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzeria.food.domain.ingredient.Ingredient;
import pizzeria.food.domain.ingredient.IngredientAlreadyInUseException;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientService;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final transient IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @PostMapping("/save")
    public ResponseEntity<Ingredient> saveIngredient(@RequestBody Ingredient ingredient){
        try {
            Ingredient saved = ingredientService.registerIngredient(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IngredientAlreadyInUseException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Ingredient> updateIngredient(@RequestBody Ingredient ingredient){
        try {
            Ingredient updated = ingredientService.updateIngredient(ingredient);
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteIngredient(@RequestBody long id) {
        try {
            ingredientService.deleteIngredient(id);
            return ResponseEntity.ok().build();
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

}
