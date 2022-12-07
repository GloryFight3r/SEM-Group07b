package pizzeria.food.domain.recipe;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Inheritance
public class Pizza extends Recipe {


    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Pizza(String name, List<Long> baseToppings, double basePrice) {
        super(name, baseToppings, basePrice);
    }

}
