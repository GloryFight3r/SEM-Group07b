package pizzeria.food.domain.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pizzeria.food.domain.HasEvents;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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



