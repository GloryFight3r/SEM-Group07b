package pizzeria.food.domain;

import java.util.List;

public class Pizza extends Recipe {
    /**
     * Empty constructor necessary for the Entity annotation
     */
    public Pizza() {
    }

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param selectedExtraToppings List of ingredients representing the selected extraToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Pizza(String name, List<Long> baseToppings, List<Long> selectedExtraToppings, double basePrice) {
        super(name, baseToppings, selectedExtraToppings, basePrice);
    }

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Pizza(String name, List<Long> baseToppings, double basePrice) {
        super(name, baseToppings, basePrice);
    }
}
