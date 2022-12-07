package pizzeria.commons;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

@Entity
public abstract class Food {
    private static int value;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private List<Ingredient> baseToppings;
    private List<Ingredient> selectedExtraToppings;
    private double basePrice;

    /**
     * Empty constructor necessary for the Entity annotation
     */
    public Food() {
    }

    /**
     * @param name String value representing the name of this Food instance
     * @param baseToppings List of ingredients representing the selected baseToppings
     * @param selectedExtraToppings List of ingredients representing the selected extraToppings
     * @param basePrice double value representing the price of the food without any extra toppings
     */
    public Food(String name, List<Ingredient> baseToppings, List<Ingredient> selectedExtraToppings, double basePrice) {
        this.id = value++;
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
    public Food(String name, List<Ingredient> baseToppings, double basePrice) {
        this.id = value++;
        this.name = name;
        this.baseToppings = baseToppings;
        this.basePrice = basePrice;
    }

    /**
     * @return an integer value representing the id of this Food object
     */
    public long getId() {
        return id;
    }

    /**
     * @param id integer value representing the new id of this Food object
     * Sets the id of this Food object to the newly specified value
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name of this Food object
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String value representing the new name of this Food object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return List of ingredients representing the selected baseToppings of this food instance
     */
    public List<Ingredient> getBaseToppings() {
        return baseToppings;
    }

    /**
     * @param baseToppings List of Ingredients representing the new selected baseToppings of this Food object
     * Sets the baseToppings field to the newly specified value
     */
    public void setBaseToppings(List<Ingredient> baseToppings) {
        this.baseToppings = baseToppings;
    }

    /**
     * @return List of Ingredients representing the selected extraToppings of this Food object
     */
    public List<Ingredient> getSelectedExtraToppings() {
        return selectedExtraToppings;
    }

    /**
     * @param selectedExtraToppings List of Ingredients representing the newly selected extraToppings of this Food object
     * Sets the selectedExtraToppings field of this Food object to the newly selected value
     */
    public void setSelectedExtraToppings(List<Ingredient> selectedExtraToppings) {
        this.selectedExtraToppings = selectedExtraToppings;
    }

    /**
     * @return a double value representing the basePrice of this Food object
     * Returns the price of this Food object without any extra toppings
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice a double value representing the new basePrice of this Food object
     * Sets the basePrice of this Food object to the newly specified value
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * @param o the object we want to compare this Food object with
     * @return true iff o is an instance of Food and has the same id as this Food object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getId() == food.getId();
    }

    /**
     * @return an integer value representing the hashCode of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * @return a double value representing the final price of this product
     */
    public double calculateFinalPrice(){
        double totalPrice = this.getBasePrice();

        for (Ingredient ingredient: this.getSelectedExtraToppings()){
            totalPrice += ingredient.getPrice();
        }
        return totalPrice;
    }
}

