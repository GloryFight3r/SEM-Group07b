package pizzeria.order.domain.coupon;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import pizzeria.order.domain.order.Order;

@Data
public abstract class Coupon {

    final String id;

    @Autowired
    public Coupon(String id) {
        //TODO: persist this thing in a JPA repo, use the id in the constructor
        this.id = id;
    }

    public abstract boolean validate(String id);
    public abstract double calculatePrice(Order order);
}
