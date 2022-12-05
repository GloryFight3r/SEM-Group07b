import java.util.List;

public class Pizza extends Food{
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
    public Pizza(String name, List<Ingredient> baseToppings, List<Ingredient> selectedExtraToppings, double basePrice) {
        super(name, baseToppings, selectedExtraToppings, basePrice);
    }

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Pizza(String name, List<Ingredient> baseToppings, double basePrice) {
        super(name, baseToppings, basePrice);
    }
}
