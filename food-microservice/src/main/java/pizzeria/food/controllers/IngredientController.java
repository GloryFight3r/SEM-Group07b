package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.food.domain.ingredient.Ingredient;
import pizzeria.food.domain.ingredient.IngredientAlreadyInUseException;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientService;
import pizzeria.food.models.ingredient.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final transient IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @PostMapping("/save")
    public ResponseEntity<SaveIngredientResponseModel> saveIngredient(@RequestBody SaveIngredientRequestModel model){
        try {
            Ingredient saved = ingredientService.registerIngredient(model.getIngredient());
            SaveIngredientResponseModel responseModel = new SaveIngredientResponseModel();
            responseModel.setId(saved.getId());
            responseModel.setIngredient(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (IngredientAlreadyInUseException e){
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateIngredientResponseModel> updateIngredient(@RequestBody UpdateIngredientRequestModel model){
        try {
            Ingredient updated = ingredientService.updateIngredient(model.getIngredient(), model.getId());
            UpdateIngredientResponseModel responseModel = new UpdateIngredientResponseModel();
            responseModel.setId(updated.getId());
            responseModel.setIngredient(updated);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteIngredient(@RequestBody DeleteIngredientRequestModel model) {
        try {
            ingredientService.deleteIngredient(model.getId());
            return ResponseEntity.ok().build();
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @GetMapping("/extraToppings")
    public ResponseEntity getExtraToppingsSet(){
        List<Ingredient> ingredientList = ingredientService.getToppingsList();
        ExtraToppingsResponseModel responseModel = new ExtraToppingsResponseModel();
        responseModel.setIngredients(ingredientList);
        return ResponseEntity.ok().body(responseModel);
    }

}
