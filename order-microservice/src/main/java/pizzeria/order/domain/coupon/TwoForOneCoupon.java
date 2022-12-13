package pizzeria.order.domain.coupon;

import pizzeria.order.domain.order.Order;

public class TwoForOneCoupon extends Coupon {
    public TwoForOneCoupon(String id) {
        super(id);
    }

    @Override
    public boolean validate(String id) {
        return false;
    }

    @Override
    public double calculatePrice(Order order) {
        return 0;
    }
}
