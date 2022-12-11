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

    @ElementCollection
    @Column(name = "ordersAssigned")
    @Getter
    private List<Long> orders;

    public Store() {}

    public Store(int id, String location) {
        this.id = id;
        this.location = location;
        this.orders = new ArrayList<>();
    }

    public Order addOrder(Order order) {
        orders.add(order.getId());

        return order;
    }

    public boolean editOrder(int orderID) {
        return false;
    }

    public boolean removeOrder(int orderID) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id && Objects.equals(location, store.location) && Objects.equals(orders, store.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location/*, orders*/);
    }
}
