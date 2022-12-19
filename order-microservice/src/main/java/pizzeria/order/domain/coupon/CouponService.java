package pizzeria.order.domain.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private transient final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public <T extends Coupon> boolean createCoupon(T coupon) {
        try {
            couponRepository.save(coupon);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
