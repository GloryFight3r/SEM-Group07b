package pizzeria.order.domain.coupon;

import com.sun.istack.NotNull;
import lombok.Getter;
import pizzeria.order.domain.order.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public abstract class Coupon {

    @Id
    @Column(name = "couponId")
    @Getter
    @NotNull
    protected String id;

    public Coupon() {
    }

    public abstract boolean validate(String id);
    public abstract double calculatePrice(Order order);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon)) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
