package pizzeria.commons;

public abstract class Coupon {

    final int id;
    final String description;

    //make default constructor for entity
    public Coupon() {
        //initialize to incorrect values to catch potential errors
        this.id = -1;
        this.description = null;
    }

    public Coupon(String description) {
        this.description = description;
        //TODO: persist this thing in a JPA repo, use the id in the constructor
        this.id = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract boolean validate(int id);
    public abstract double calculatePrice(Order order);
}
