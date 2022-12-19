package pizzeria.order.domain.coupon;

import pizzeria.order.domain.order.Order;

public class TwoForOneCoupon extends Coupon {

    public TwoForOneCoupon() {
        super();
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
