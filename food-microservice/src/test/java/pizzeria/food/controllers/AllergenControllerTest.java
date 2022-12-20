package pizzeria.food.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.food.domain.Allergens.AllergenService;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.recipe.Recipe;
import pizzeria.food.models.allergens.FilterMenuRequestModel;
import pizzeria.food.models.allergens.FilterMenuResponseModel;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
class AllergenControllerTest {
    private transient AllergenController allergenController;
    private transient AllergenService allergenService;

    @BeforeEach
    void setUp(){
        allergenService = Mockito.mock(AllergenService.class);
        allergenController = new AllergenController(allergenService);
    }
    @Test
    void filterMenu() {
        List<String> allergens = List.of("gluten", "lactose");
        Recipe recipe = new Recipe("Test", List.of(1L, 55L), 12.0);
        Recipe recipe1 = new Recipe("Test1", List.of(1L, 55L), 13.0);
        List<Recipe> result = List.of(recipe, recipe1);
        try {
            when(allergenService.filterMenuOnAllergens(allergens)).thenReturn(result);
            FilterMenuRequestModel requestModel = new FilterMenuRequestModel();
            requestModel.setAllergens(allergens);
            assertThat(allergenController.filterMenu(requestModel).getBody().getRecipes()).isEqualTo(result);
        } catch (IngredientNotFoundException e) {
            fail();
        }
    }

    @Test
    void testFilterMenuThrowsException(){
        List<String> allergens = List.of("gluten", "lactose");
        try {
            when(allergenService.filterMenuOnAllergens(allergens)).thenThrow(IngredientNotFoundException.class);
            FilterMenuRequestModel requestModel = new FilterMenuRequestModel();
            requestModel.setAllergens(allergens);
            ResponseEntity<FilterMenuResponseModel> response = allergenController.filterMenu(requestModel);
            assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertTrue(response.getHeaders().containsKey(HttpHeaders.WARNING));
        } catch (IngredientNotFoundException e) {
            fail();
        }

    }
}