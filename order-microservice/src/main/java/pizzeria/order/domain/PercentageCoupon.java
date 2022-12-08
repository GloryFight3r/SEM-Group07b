package pizzeria.order.domain;

public class PercentageCoupon extends Coupon {
    public PercentageCoupon(String id) {
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
