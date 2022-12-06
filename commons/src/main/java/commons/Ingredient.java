package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Ingredient {
    private static int value = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private double price;
    private List<String> allergens;


    /**
     * Empty constructor necessary for the Entity annotation
     */
    public Ingredient() {
    }

    /**
     * @param name String value representing the name of this commons.Ingredient
     * @param price Double value representing the price of this commons.Ingredient
     * @param allergens List of Strings representing the allergies this commons.Ingredient contains
     * returns a new commons.Ingredient with the values specified above
     */
    public Ingredient(String name, double price, List<String> allergens) {
        id = value;
        value++;
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

    /**
     * @return a long value representing the id of this commons.Ingredient
     */
    public long getId() {
        return id;
    }

    /**
     * @param id long value representing the new id of this commons.Ingredient
     * Sets the id of this commons.Ingredient to the newly specified long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return a String value representing the name of this commons.Ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String value representing the new name of this commons.Ingredient
     * sets the name of this commons.Ingredient to the newly specified string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return double value representing the price of this commons.Ingredient
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price double value representing the new price of this commons.Ingredient
     * Sets the price of this commons.Ingredient to the specified value
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return a list of Strings that represents the allergens of this commons.Ingredient
     */
    public List<String> getAllergens() {
        return allergens;
    }

    /**
     * @param allergens list of Strings representing the new allergens of this commons.Ingredient
     * Sets the allergens of this commons.Ingredient to the newly specified list
     */
    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    /**
     * We check this commons.Ingredient for equality with object o
     * @param o represents the object we want to compare for equality with
     * @return true iff o is an commons.Ingredient and has the same id as this commons.Ingredient
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return this.getId() == that.getId();
    }

    /**
     * @return an integer representing the hashcode of this commons.Ingredient
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getAllergens());
    }
}

