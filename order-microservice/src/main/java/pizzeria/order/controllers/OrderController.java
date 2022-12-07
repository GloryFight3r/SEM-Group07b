package pizzeria.order.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.order.domain.Order;
import pizzeria.order.domain.OrderService;

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
        // TODO: validate user token
        // TODO: validate the foods
        Order saved = null;
        try {
            saved = orderService.saveOrder(incoming);
        } catch (Exception e) {
            return ResponseEntity.badRequest().header(HttpHeaders.WARNING, e.getMessage()).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
