package pizzeria.commons;

import pizzeria.commons.Ingredient;
import pizzeria.commons.Pizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PizzaTest {
    Pizza pizza;

    @BeforeEach
    void setUp(){
        List<Ingredient> base = new ArrayList<>();
        base.add(new Ingredient("Sauce", 5.0));
        base.add(new Ingredient("Cheese", 2.0));
        List<Ingredient> extraToppings = new ArrayList<>();
        extraToppings.add(new Ingredient("Pineapple", 2.0));
        pizza = new Pizza("Hawaii", base, extraToppings, 10.0);
    }

    @Test
    void testGetId(){
        pizza.setId(10);
        assertEquals(10, pizza.getId());
    }

    @Test
    void testSetId(){
        pizza.setId(101);
        assertEquals(101, pizza.getId());
    }

    @Test
    void testGetName(){
        assertEquals("Hawaii", pizza.getName());
    }

    @Test
    void testSetName(){
        pizza.setName("Margherita");
        assertEquals("Margherita", pizza.getName());
    }

    @Test
    void testGetBaseToppings(){
        List<Ingredient> base = new ArrayList<>();
        base.add(new Ingredient("Sauce", 5.0));
        base.add(new Ingredient("Cheese", 2.0));
        assertEquals(base, pizza.getBaseToppings());
    }

    @Test
    void testSetBaseToppings(){
        List<Ingredient> base = new ArrayList<>();
        base.add(new Ingredient("Sauce", 5.0));
        base.add(new Ingredient("Cheese", 2.0));
        base.add(new Ingredient("Ham", 2.0));
        pizza.setBaseToppings(base);
        List<Ingredient> baseCheck = new ArrayList<>();
        baseCheck.add(new Ingredient("Sauce", 5.0));
        baseCheck.add(new Ingredient("Cheese", 2.0));
        baseCheck.add(new Ingredient("Ham", 2.0));
        assertEquals(baseCheck, pizza.getBaseToppings());
    }

    @Test
    void testGetBasePrice(){
        assertEquals(10.0, pizza.getBasePrice());
    }

    @Test
    void testSetBasePrice(){
        pizza.setBasePrice(11.0);
        assertEquals(11.0, pizza.getBasePrice());
    }

    @Test
    void testEquals1(){
        assertTrue(pizza.equals(pizza));
    }

    @Test
    void testEquals2(){
        Pizza pizza2 = new Pizza();
        assertFalse(pizza2.equals(pizza));
    }

    @Test
    void testEquals3(){
        Pizza pizza2 = new Pizza();
        pizza.setId(0);
        pizza2.setId(0);
        assertEquals(pizza2, pizza);
    }

    @Test
    void testHashCode1(){
        Pizza pizza2 = new Pizza();
        assertNotEquals(pizza2.hashCode(), pizza.hashCode());
    }


}