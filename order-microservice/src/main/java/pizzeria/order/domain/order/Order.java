package pizzeria.order.domain.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.food.FoodPriceService;
import pizzeria.order.models.GetPricesRequestModel;
import pizzeria.order.models.GetPricesResponseModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @Column(name = "orderId")
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long orderId;

    @OneToMany(mappedBy="orderId")
    @Getter
    private List<Food> foods;

    @Column(name = "store_id")
    @Getter
    private long storeId;

    @Column(name = "user_id")
    @Getter
    private long userId;

    @Column(name = "pickup_time")
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    private transient FoodPriceService foodPriceService;

    @Autowired
    public Order(FoodPriceService foodPriceService) {
        this.foodPriceService = foodPriceService;
    }

    //default constructor
    public Order() {}

    @SuppressWarnings("PMD")
    public double calculatePrice() {
        GetPricesResponseModel prices = foodPriceService.getFoodPrices(this);

        if (couponIds.isEmpty()) {
            // If coupon list is empty, just add all ingredients and recipes
            for (Food f: foods) {

            }
        }
        double min = Double.MAX_VALUE;

        long couponUsed = couponIds.get(0);
        //TODO: send request to get all the food prices
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
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
