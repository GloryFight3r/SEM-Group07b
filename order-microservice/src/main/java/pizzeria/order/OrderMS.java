package pizzeria.order;

import pizzeria.commons.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class OrderMS {
    public static void main(String[] args) {
        SpringApplication.run(OrderMS.class, args);
    }
    private Order order; // works? works. hopefully...
}
