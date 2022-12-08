package pizzeria.food.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
public abstract class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;
    @Getter
    private String name;
    @ElementCollection
    @Getter
    private List<Long> baseToppings;
    @ElementCollection
    @Getter
    private List<Long> selectedExtraToppings;
    @Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

