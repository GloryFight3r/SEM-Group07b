package pizzeria.food.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private double price;
    @ElementCollection
    private List<String> allergens;


    /**
     * @param name String value representing the name of this commons.Ingredient
     * @param price Double value representing the price of this commons.Ingredient
     * @param allergens List of Strings representing the allergies this commons.Ingredient contains
     * returns a new commons.Ingredient with the values specified above
     */
    public Ingredient(String name, double price, List<String> allergens) {
        this.name = name;
        this.price = price;
        this.allergens = allergens;
    }

    /**
     * @param name String value representing the name of this commons.Ingredient
     * @param price Double value representing the price of this commons.Ingredient
     * returns a new commons.Ingredient that contains no allergens
     */
    public Ingredient(String name, double price) {
        this.name = name;
        this.price = price;
        this.allergens = new ArrayList<>();
    }
}

