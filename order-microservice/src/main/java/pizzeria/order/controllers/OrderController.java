package pizzeria.order.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.order.domain.order.Order;
import pizzeria.order.domain.order.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final transient OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order incoming) {
        // TODO: validate user token (also check that user id from token matches the one from order)
        try {
            Order processed = orderService.processOrder(incoming);
            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Order> editOrder(@RequestBody Order incoming) {
        // TODO: validate user token (also check that user id from token matches the one from order)
        try {
            Order processed = orderService.processOrder(incoming);
            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Order> deleteOrder(@RequestBody Long orderId) {
        // TODO: during the jwt validation we keep the user id in a var and the isManager bool
        Long userId = null;
        boolean isManager = false;
        if (orderService.removeOrder(orderId, userId, isManager))
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Order>> listOrders() {
        // TODO: JWT validation (keep user id)
        Long userId = null;
        List<Order> orders = orderService.listOrders(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Order>> listAllOrders() {
        // TODO: JWT validation (keep isManager)
        boolean isManager = false;
        if (!isManager)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        List<Order> orders = orderService.listAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
