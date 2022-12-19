package pizzeria.order.domain.store;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import pizzeria.order.domain.order.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="stores")
public class Store {

    @Id
    @Column(name = "id")
    @NotNull
    @Getter
    @Setter
    private long id;

    @Column(name = "location")
    @Getter
    @Setter
    private String location;

    @Column(name = "contact")
    @Getter
    @Setter
    private String contact;

    @ElementCollection
    @Column(name = "ordersAssigned")
    @Getter
    private List<Long> orders;

    public Store() {}

    public Store(int id, String location, String contact) {
        this.id = id;
        this.location = location;
        this.contact = contact;
        this.orders = new ArrayList<>();
    }

    public Order addOrder(Order order) {
        orders.add(order.getId());

        return order;
    }

    public boolean removeOrder(long orderID) {
        return orders.remove(orderID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return id == store.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
