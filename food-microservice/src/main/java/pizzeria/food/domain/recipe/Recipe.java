package pizzeria.food.domain.recipe;
import lombok.Data;
import lombok.NoArgsConstructor;
import pizzeria.food.domain.HasEvents;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Recipe extends HasEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ElementCollection
    private List<Long> baseToppings;
    private double basePrice;
    private FoodType foodType = FoodType.PIZZA;


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
}



