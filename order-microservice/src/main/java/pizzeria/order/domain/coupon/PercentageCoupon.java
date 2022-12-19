package pizzeria.order.domain.coupon;

import lombok.Getter;
import pizzeria.order.domain.order.Order;

public class PercentageCoupon extends Coupon {
    @Getter
    private double percentage;

    public PercentageCoupon() {
    }

    @Override
    public boolean validate(String id) {
        return false;
    }

    @Override
    public double calculatePrice(Order order) {
        return 0; // TODO: meth
    }
}
