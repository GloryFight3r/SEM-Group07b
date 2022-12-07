package pizzeria.food.domain;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public abstract class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ElementCollection
    private List<Long> baseToppings;
    @ElementCollection
    private List<Long> selectedExtraToppings;
    private double basePrice;

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param selectedExtraToppings List of ingredients representing the selected extraToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Recipe(String name, List<Long> baseToppings, List<Long> selectedExtraToppings, double basePrice) {
        this.name = name;
        this.baseToppings = baseToppings;
        this.selectedExtraToppings = selectedExtraToppings;
        this.basePrice = basePrice;
    }

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Recipe(String name, List<Long> baseToppings, double basePrice) {
        this.name = name;
        this.baseToppings = baseToppings;
        this.basePrice = basePrice;
    }

    /**
     * @return a double value representing the final price of this product
     */
    public double calculateFinalPrice(){
        double totalPrice = this.getBasePrice();

        for (Long ingredientId: this.getSelectedExtraToppings()){
            // TODO get the ingredient from the ingredient database
            totalPrice += 1;
            //totalPrice += ingredient.getPrice();
        }
        return totalPrice;
    }
}

