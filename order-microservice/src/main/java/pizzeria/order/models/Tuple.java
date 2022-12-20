package pizzeria.order.models;

/**
 * The type Tuple to store price matched with a name
 * used for recipes and ingredients
 */
public class Tuple {
    private double price;
    private String name;

    /**
     * Instantiates a new Tuple.
     *
     * @param price the price of the recipe/ingredient
     * @param name  the name of the recipe/ingredient
     */
    public Tuple(double price, String name) {
        this.price = price;
        this.name = name;
    }

    /**
     * Gets price of recipe/ingredient
     *
     * @return the price of recipe/ingredient
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price of recipe/ingredient
     *
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets name of recipe/ingredient
     *
     * @return the name of recipe/ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of recipe/ingredient
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
}
