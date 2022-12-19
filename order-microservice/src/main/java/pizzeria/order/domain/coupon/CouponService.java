package pizzeria.order.domain.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Coupon service.
 */
@Service
public class CouponService {
    private transient final CouponRepository couponRepository;

    /**
     * Instantiates a new Coupon service with the respective repositories
     *
     * @param couponRepository the coupon repository (JpaRepo)
     */
    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * Creates a coupon in the database
     *
     * @param <T>    the type parameter (any subclass of coupon)
     * @param coupon the coupon to be created
     * @return the boolean
     */
    public <T extends Coupon> boolean createCoupon(T coupon) {
        try {
            //we save the coupon in the database (unless anything goes wrong)
            couponRepository.save(coupon);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
