package pizzeria.order.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.order.authentication.AuthManager;
import pizzeria.order.domain.order.Order;
import pizzeria.order.domain.order.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final transient AuthManager authManager;
    private final transient OrderService orderService;

    @Autowired
    public OrderController(AuthManager authManager, OrderService orderService){
        this.authManager = authManager;
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order incoming) {
        try {
            String userId = authManager.getNetId();
            if (!userId.equals(incoming.getUserId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, "You are trying to edit an order from someone else").build();
            }
            Order processed = orderService.processOrder(incoming);
            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Order> editOrder(@RequestBody Order incoming) {
        try {
            String userId = authManager.getNetId();
            if (!userId.equals(incoming.getUserId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, "You are trying to edit an order from someone else").build();
            }
            Order processed = orderService.processOrder(incoming);
            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Order> deleteOrder(@RequestBody Long orderId) {
        String userId = authManager.getNetId();
        boolean isManager = authManager.getRole().equals("[ROLE_MANAGER]");
        if (orderService.removeOrder(orderId, userId, isManager))
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Order>> listOrders() {
        String userId = authManager.getNetId();
        List<Order> orders = orderService.listOrders(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Order>> listAllOrders() {
        List<Order> orders = orderService.listAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
