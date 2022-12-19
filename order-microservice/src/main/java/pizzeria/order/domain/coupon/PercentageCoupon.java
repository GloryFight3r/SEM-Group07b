package pizzeria.order.domain.coupon;

import lombok.Getter;
import pizzeria.order.domain.order.Order;

public class PercentageCoupon extends Coupon {
    @Getter
    private final double percentage;
    public PercentageCoupon(String id, double percentage) {
        super(id);
        this.percentage = percentage;
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
