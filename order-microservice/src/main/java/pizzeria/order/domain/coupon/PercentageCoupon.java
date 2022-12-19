package pizzeria.order.domain.coupon;

import lombok.Getter;
import pizzeria.order.domain.order.Order;
import pizzeria.order.models.GetPricesResponseModel;

/**
 * The type Percentage coupon.
 */
public class PercentageCoupon extends Coupon {
    @Getter
    private double percentage;

    /**
     * Empty constructor, database purposes
     */
    public PercentageCoupon() {
        super();
    }

    @Override
    public double calculatePrice(Order order, GetPricesResponseModel prices, double basePrice) {
        //makes the percentage reduction on the base price
        return basePrice - basePrice * percentage;
    }
}
