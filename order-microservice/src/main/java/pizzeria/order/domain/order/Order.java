package pizzeria.order.domain.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Getter;
import pizzeria.order.domain.food.Food;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @Column(name = "orderId")
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    protected Long orderId;

    @OneToMany(mappedBy="orderId")
    @Getter
    private List<Food> foods;

    @Column(name = "store_id")
    @Getter
    private long storeId;

    @Column(name = "user_id")
    @Getter
    private String userId;

    @Column(name = "pickup_time")
    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pickupTime;

    @Column(name = "price")
    @Getter
    protected double price;

    @ElementCollection
    @CollectionTable(name = "couponIds",
        joinColumns = @JoinColumn(name = "id"))
    @Column(name = "couponIds")
    @Getter
    protected List<String> couponIds;

    //default constructor
    public Order() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
