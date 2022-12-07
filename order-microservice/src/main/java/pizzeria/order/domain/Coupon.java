package pizzeria.order.domain;

import lombok.Data;

@Data
public abstract class Coupon {

    final String id;

    public Coupon(String id) {
        //TODO: persist this thing in a JPA repo, use the id in the constructor
        this.id = id;
    }

    public abstract boolean validate(String id);
    public abstract double calculatePrice(Order order);
}
