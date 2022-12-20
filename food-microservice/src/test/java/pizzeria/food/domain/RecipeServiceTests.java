package pizzeria.food.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.food.domain.ingredient.Ingredient;
import pizzeria.food.domain.ingredient.IngredientNotFoundException;
import pizzeria.food.domain.ingredient.IngredientRepository;
import pizzeria.food.domain.recipe.*;
import pizzeria.food.models.prices.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RecipeServiceTests {
    @Autowired
    private transient RecipeRepository recipeRepository;

    @Autowired
    private transient IngredientRepository ingredientRepository;

    @Autowired
    private transient RecipeService recipeService;



    @Test
    void registerFood() {
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L, 3L), 12.0);
        try {
            Recipe rec = recipeService.registerFood(recipe);
            assertTrue(recipeRepository.existsById(rec.getId()));
            List<Recipe> test = recipeRepository.findAll();
            assertThat(test.size()).isEqualTo(1);
            assertThat(test.get(0).getName()).isEqualTo("test");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void registerFood2(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L, 3L), 12.0);
        Recipe recipe1 = new Recipe("test2", List.of(2L, 3L), 12.0);
        try {
            Recipe rec = recipeService.registerFood(recipe);
            Recipe rec1 = recipeService.registerFood(recipe1);
            assertTrue(recipeRepository.existsById(rec.getId()));
            assertTrue(recipeRepository.existsById(rec1.getId()));
            List<Recipe> test = recipeRepository.findAll();
            assertThat(test.size()).isEqualTo(2);
            assertThat(test.get(0).getName()).isEqualTo("test");
            assertThat(test.get(1).getName()).isEqualTo("test2");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void registerFoodThrowsException(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test4", 1.0, new ArrayList<>()));

        Recipe recipe = new Recipe("test", List.of(3L, 2L, 4L, 12L), 12.0);
        assertThrows(IngredientNotFoundException.class, () -> recipeService.registerFood(recipe));
    }

    @Test
    void registerFoodThrowsException2(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L, 3L), 12.0);
        Recipe recipe1 = new Recipe("test", List.of(2L, 3L), 12.0);

        assertThrows(RecipeAlreadyInUseException.class, () -> {
            recipeService.registerFood(recipe);
            recipeService.registerFood(recipe1);
        });
    }


    @Test
    void updateFood() {
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L), 12.0);
        try {
            recipe = recipeService.registerFood(recipe);
            recipe.setName("testName");
            recipe.setBaseToppings(List.of(1L, 2L, 3L));
            recipeService.updateFood(recipe, recipe.getId());
            assertTrue(recipeRepository.existsById(recipe.getId()));
            List<Recipe> test = recipeRepository.findAll();
            assertThat(test.size()).isEqualTo(1);
            assertThat(test.get(0).getName()).isEqualTo("testName");
            assertThat(test.get(0).getBaseToppings()).containsExactlyElementsOf(List.of(1L, 2L, 3L));
        } catch (RecipeAlreadyInUseException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        } catch (IngredientNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        } catch (RecipeNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void updateFoodThrowsIngredientNotFoundException(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L), 12.0);
        assertThrows(IngredientNotFoundException.class, () -> {
            recipeService.registerFood(recipe);
            recipe.setId(1L);
            recipe.setBaseToppings(List.of(1L, 2L, 3L, 22L));
            recipeService.updateFood(recipe, recipe.getId());
        });
    }

    @Test
    void updateFoodThrowsRecipeNotFoundException(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L), 12.0);

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.registerFood(recipe);
            recipe.setId(2L);
            recipe.setBaseToppings(List.of(1L, 2L, 3L));
            recipeService.updateFood(recipe, recipe.getId());
        });
    }

    @Test
    void deleteFood() {
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        Recipe recipe = new Recipe("test", List.of(1L, 2L), 12.0);
        try {
            recipe = recipeService.registerFood(recipe);
            recipeService.deleteFood(recipe.getId());
            assertFalse(recipeRepository.existsById(recipe.getId()));
            List<Recipe> test = recipeRepository.findAll();
            assertThat(test.size()).isEqualTo(0);
        } catch (RecipeNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        } catch (IngredientNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        } catch (RecipeAlreadyInUseException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void deleteFoodIdIsNotInUse(){
        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteFood(1L));
    }

    @Test
    void getPrices() {
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test4", 1.0, new ArrayList<>()));
        recipeRepository.save(new Recipe("recipe1", List.of(1L), 18.0));
        recipeRepository.save(new Recipe("recipe2", List.of(1L, 2L), 2.0));
        recipeRepository.save(new Recipe("recipe3", List.of(1L, 2L, 3L), 22.0));
        recipeRepository.save(new Recipe("recipe4", List.of(1L, 2L, 3L, 4L), 10.0));
        recipeRepository.save(new Recipe("recipe5", List.of(2L), 19.0));
        recipeRepository.save(new Recipe("recipe6", List.of(2L, 3L), 17.0));
        recipeRepository.save(new Recipe("recipe7", List.of(2L, 3L, 4L), 14.0));
        try {
            Map<Long, Tuple> result = recipeService.getPrices(List.of(5L, 6L, 7L, 8L, 9L, 10L, 11L));
            assertThat(result.size()).isEqualTo(7);
            assertThat(result.get(5L).getPrice()).isEqualTo(18.0);
            assertThat(result.get(6L).getPrice()).isEqualTo(2.0);
            assertThat(result.get(7L).getPrice()).isEqualTo(22.0);
            assertThat(result.get(8L).getPrice()).isEqualTo(10.0);
            assertThat(result.get(9L).getPrice()).isEqualTo(19.0);
            assertThat(result.get(10L).getPrice()).isEqualTo(17.0);
            assertThat(result.get(11L).getPrice()).isEqualTo(14.0);
            assertThat(result.get(5L).getName()).isEqualTo("recipe1");
            assertThat(result.get(6L).getName()).isEqualTo("recipe2");
            assertThat(result.get(7L).getName()).isEqualTo("recipe3");
            assertThat(result.get(8L).getName()).isEqualTo("recipe4");
            assertThat(result.get(9L).getName()).isEqualTo("recipe5");
            assertThat(result.get(10L).getName()).isEqualTo("recipe6");
            assertThat(result.get(11L).getName()).isEqualTo("recipe7");
        } catch (RecipeNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void getPricesThrowsRecipeNotFoundException(){
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getPrices(List.of(1L, 2L, 3L)));
    }

    @Test
    void getPricesThrowsRecipeNotFoundException2(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test4", 1.0, new ArrayList<>()));
        recipeRepository.save(new Recipe("recipe1", List.of(1L), 18.0));
        recipeRepository.save(new Recipe("recipe2", List.of(1L, 2L), 2.0));
        recipeRepository.save(new Recipe("recipe3", List.of(1L, 2L, 3L), 22.0));
        recipeRepository.save(new Recipe("recipe4", List.of(1L, 2L, 3L, 4L), 10.0));
        recipeRepository.save(new Recipe("recipe5", List.of(2L), 19.0));
        recipeRepository.save(new Recipe("recipe6", List.of(2L, 3L), 17.0));
        recipeRepository.save(new Recipe("recipe7", List.of(2L, 3L, 4L), 14.0));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getPrices(List.of(5L, 6L, 7L, 88L, 9L, 10L, 11L)));
    }

    @Test
    void testGetPrices2(){
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test4", 1.0, new ArrayList<>()));
        recipeRepository.save(new Recipe("recipe1", List.of(1L), 18.0));
        recipeRepository.save(new Recipe("recipe2", List.of(1L, 2L), 2.0));
        recipeRepository.save(new Recipe("recipe3", List.of(1L, 2L, 3L), 22.0));
        recipeRepository.save(new Recipe("recipe4", List.of(1L, 2L, 3L, 4L), 10.0));
        recipeRepository.save(new Recipe("recipe5", List.of(2L), 19.0));
        recipeRepository.save(new Recipe("recipe6", List.of(2L, 3L), 17.0));
        recipeRepository.save(new Recipe("recipe7", List.of(2L, 3L, 4L), 14.0));

        try {
            Map<Long, Tuple> result = recipeService.getPrices(List.of(5L, 7L, 8L, 10L, 11L));
            assertThat(result.size()).isEqualTo(5);
            assertThat(result.get(5L).getPrice()).isEqualTo(18.0);
            assertThat(result.get(7L).getPrice()).isEqualTo(22.0);
            assertThat(result.get(8L).getPrice()).isEqualTo(10.0);
            assertThat(result.get(10L).getPrice()).isEqualTo(17.0);
            assertThat(result.get(11L).getPrice()).isEqualTo(14.0);
            assertThat(result.get(5L).getName()).isEqualTo("recipe1");
            assertThat(result.get(7L).getName()).isEqualTo("recipe3");
            assertThat(result.get(8L).getName()).isEqualTo("recipe4");
            assertThat(result.get(10L).getName()).isEqualTo("recipe6");
            assertThat(result.get(11L).getName()).isEqualTo("recipe7");
        } catch (RecipeNotFoundException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void getMenu() {
        ingredientRepository.save(new Ingredient("test1", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test2", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test3", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test4", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test5", 1.0, new ArrayList<>()));
        ingredientRepository.save(new Ingredient("test6", 1.0, new ArrayList<>()));
        recipeRepository.save(new Recipe("recipe1", List.of(1L), 18.0));
        recipeRepository.save(new Recipe("recipe2", List.of(6L, 3L), 2.0));
        recipeRepository.save(new Recipe("recipe3", List.of(1L, 2L, 3L), 22.0));
        recipeRepository.save(new Recipe("recipe4", List.of(5L, 3L, 6L, 4L), 10.0));
        recipeRepository.save(new Recipe("recipe5", List.of(2L), 19.0));
        recipeRepository.save(new Recipe("recipe6", List.of(2L, 3L), 17.0));
        recipeRepository.save(new Recipe("recipe7", List.of(2L, 3L, 4L), 14.0));
        recipeRepository.save(new Recipe("recipe8", List.of(1L, 2L, 3L, 4L, 5L, 6L), 10.0));

        List<Recipe> result = recipeService.getMenu();
        assertThat(result.size()).isEqualTo(8);
        assertThat(result.get(0).getName()).isEqualTo("recipe1");
        assertThat(result.get(1).getName()).isEqualTo("recipe2");
        assertThat(result.get(2).getName()).isEqualTo("recipe3");
        assertThat(result.get(3).getName()).isEqualTo("recipe4");
        assertThat(result.get(4).getName()).isEqualTo("recipe5");
        assertThat(result.get(5).getName()).isEqualTo("recipe6");
        assertThat(result.get(6).getName()).isEqualTo("recipe7");
        assertThat(result.get(7).getName()).isEqualTo("recipe8");

    }

    @Test
    void getMenu2(){
        List<Recipe> result = recipeService.getMenu();
        assertThat(result.size()).isEqualTo(0);
    }

}
