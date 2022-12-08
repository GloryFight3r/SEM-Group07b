package pizzeria.order.domain.store;

import com.sun.istack.NotNull;
import lombok.Getter;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Column(name = "location")
    @Getter
    private String location; // attribute data type to be decided

//    @ElementCollection
//    @Column(name = "ordersAssigned")
//    @Getter
//    private List<Order> orders;

    public Store() {}

    public Store(int id, String location) {
        this.id = id;
        this.location = location;
//        this.orders = new ArrayList<>(); // attribute data type to be decided
    }

    public Order addOrder(Order order) {
//        orders.add(order);

        return order;
    }

    public boolean editOrder(int orderID) {
        return false;
    }

    public boolean removeOrder(int orderID) {
        return false;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id && Objects.equals(location, store.location); // && Objects.equals(orders, store.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location/*, orders*/);
    }
}
