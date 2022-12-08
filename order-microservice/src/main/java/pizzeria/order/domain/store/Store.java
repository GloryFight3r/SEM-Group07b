package pizzeria.order.domain.store;

import pizzeria.order.domain.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Store {

    private static int value;

    private int id;
    private String location; // attribute data type to be decided
    private List<Order> orders;

    public Store() {}

    public Store(String location) {
        this.id = value++;
        this.location = location;
        this.orders = new ArrayList<>(); // attribute data type to be decided
    }

    public Order addOrder(Order order) {
        orders.add(order);

        return order;
    }

    public boolean editOrder(int orderID) {
        return false;
    }

    public boolean removeOrder(int orderID) {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        return Objects.hash(id, location, orders);
    }
}
