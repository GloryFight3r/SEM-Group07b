package pizzeria.commons;

import java.util.*;
import java.time.LocalDateTime;

public class Order {

    List<Food> foodList;
    int id;
    int storeId;
    int userId;
    LocalDateTime pickupTime;
    double price;
    List<Integer> couponIds;

    //TODO: make default constructor for persisting as entity

    public List<Food> getFoodList() {
        return this.foodList;
    }

    public int getId() {
        return this.id;
    }

    public int getStoreId() {
        return this.storeId;
    }

    public int getUserId() {
        return this.userId;
    }

    public LocalDateTime getPickupTime() {
        return this.pickupTime;
    }

    public double getPrice() {
        return this.price;
    }

    public List<Integer> getCouponIds() {
        return this.couponIds;
    }

    public double calculatePrice() {
        double min = Double.MAX_VALUE;
        int couponUsed = -1;
        for (int c : couponIds) {
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
