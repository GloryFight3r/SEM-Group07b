package pizzeria.order;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import pizzeria.commons.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pizzeria.order", "pizzeria.commons"})
@EntityScan("pizzeria.commons")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    private Order order; // works? works. hopefully...
}

