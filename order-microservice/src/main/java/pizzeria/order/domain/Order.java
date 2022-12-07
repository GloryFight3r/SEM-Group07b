package pizzeria.order.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ElementCollection
    private List<Long> foodList;
    private long storeId;
    private long userId;
    private LocalDateTime pickupTime;
    private double price;
    @ElementCollection
    private List<Long> couponIds;

    //default constructor
    public Order(){

    }
    @SuppressWarnings("PMD")
    public double calculatePrice() {
        if (couponIds.isEmpty()) {
            this.price = -1;
            return -1;
        }
        double min = Double.MAX_VALUE;

        long couponUsed = couponIds.get(0);

        for (long c : couponIds) {
            //TODO: query Jpa repository for the coupon
            //validate the coupon (part of the query) and calculate a price if not null
            double price = 5.0;
            if (price < min) {
                min = price;
                couponUsed = c;
            }
        }

        //update the coupon list with the actual coupon used
        this.couponIds.clear();
        //if there was a valid coupon used we add it to the list (for the response body)
        couponIds.add(couponUsed);

        this.price = min;

        return min;
    }
}
