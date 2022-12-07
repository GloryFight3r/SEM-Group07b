package pizzeria.order.domain;

public class PercentageCoupon extends Coupon {

    @Override
    public boolean validate(int id) {
        return false;
    }

    @Override
    public double calculatePrice(Order order) {
        return 0;
    }
}
