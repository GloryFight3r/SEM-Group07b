import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    Ingredient ingredient;
    List<String> allergens;
    @BeforeEach
    void setUp(){
        allergens = new ArrayList<>();
        allergens.add("a");
        allergens.add("b");
        ingredient = new Ingredient("Cheese", 2.0, allergens);
    }

    @Test
    void testConstructor0(){
        Ingredient i = new Ingredient();
        assertNotNull(i);
    }

    @Test
    void testConstructor1(){
        Ingredient i = new Ingredient("ch", 2.0);
        assertTrue(i != null && i.getAllergens().size() == 0);
    }

    @Test
    void testConstructor2(){
        assertTrue(ingredient != null && ingredient.getAllergens().size() == 2);
    }
    @Test
    void getId() {
        ingredient.setId(100);
        assertEquals(100, ingredient.getId());
    }

    @Test
    void setId() {
        ingredient.setId(100);
        assertEquals(100, ingredient.getId());
    }

    @Test
    void getName() {
        assertEquals("Cheese", ingredient.getName());
    }

    @Test
    void setName() {
        ingredient.setName("Onion");
        assertEquals("Onion", ingredient.getName());
    }

    @Test
    void getPrice() {
        assertEquals(2.0, ingredient.getPrice());
    }

    @Test
    void setPrice() {
        ingredient.setPrice(5.0);
        assertEquals(5.0, ingredient.getPrice());
    }

    @Test
    void getAllergens() {
        List<String> check = new ArrayList<>();
        check.add("a");
        check.add("b");
        assertEquals(check, ingredient.getAllergens());
    }

    @Test
    void setAllergens() {
        List<String> check = new ArrayList<>();
        check.add("c");
        ingredient.setAllergens(check);
        List<String> compare = new ArrayList<>();
        compare.add("c");
        assertEquals(compare, ingredient.getAllergens());
    }

    @Test
    void testEquals1() {
        assertTrue(ingredient.equals(ingredient));
    }

    @Test
    void testEquals2(){
        Ingredient ingredient1 = new Ingredient("Onion", 2.0);
        assertFalse(ingredient1.equals(ingredient));
    }

    @Test
    void testEquals3(){
        Ingredient ingredient2 = new Ingredient("Cheese", 2.0);
        ingredient2.setId(ingredient.getId());
        assertEquals(ingredient2, ingredient);
    }

    @Test
    void testHashCode() {
        Ingredient ingredient2 = new Ingredient("Cheese", 2.0);
        assertNotEquals(ingredient2.hashCode(), ingredient.hashCode());
    }


}