package pizzeria.order.domain;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @Column(name = "id")
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @ElementCollection
    @CollectionTable(name = "foodIds",
        joinColumns = @JoinColumn(name = "id"))
    @Column(name = "foodIds")
    @Getter
    private List<Long> foodIds;

    @Column(name = "store_id")
    @Getter
    private long storeId;

    @Column(name = "user_id")
    @Getter
    private long userId;

    @Column(name = "pickup_time")
    @Getter
    private LocalDateTime pickupTime;

    @Column(name = "price")
    @Getter
    private double price;

    @ElementCollection
    @CollectionTable(name = "couponIds",
        joinColumns = @JoinColumn(name = "id"))
    @Column(name = "couponIds")
    @Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
