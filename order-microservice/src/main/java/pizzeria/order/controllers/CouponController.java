package pizzeria.order.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzeria.order.domain.coupon.CouponService;
import pizzeria.order.domain.coupon.Coupon;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    private final transient CouponService couponService;

    @Autowired
    public CouponController(CouponService orderService) {
        this.couponService = orderService;
    }

    @PostMapping("/create")
    public <T extends Coupon> ResponseEntity<Void> createCoupon(@RequestBody T coupon) {
        // TODO: validate user token (also check that user id from token matches the one from order)
        boolean isManager = true;
        if (isManager && couponService.createCoupon(coupon))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}