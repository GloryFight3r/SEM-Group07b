package pizzeria.commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private List<Food> foodList;
    private long storeId;
    private long userId;
    private LocalDateTime pickupTime;
    private double price;
    private List<Long> couponIds;

    //default constructor
    public Order(){

    }

    public List<Food> getFoodList() {
        return this.foodList;
    }

    public long getId() {
        return this.id;
    }

    public long getStoreId() {
        return this.storeId;
    }

    public long getUserId() {
        return this.userId;
    }

    public LocalDateTime getPickupTime() {
        return this.pickupTime;
    }

    public double getPrice() {
        return this.price;
    }

    public List<Long> getCouponIds() {
        return this.couponIds;
    }

    public double calculatePrice() {
        double min = Double.MAX_VALUE;
        long couponUsed = -1;
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
        if (couponUsed != -1) {
            //if there was a valid coupon used we add it to the list (for the response body)
            couponIds.add(couponUsed);
        }

        return min;
    }
}
