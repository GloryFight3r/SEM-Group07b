package pizzeria.order.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizzeria.order.authentication.AuthManager;
import pizzeria.order.domain.mailing.MailingService;
import pizzeria.order.domain.order.Order;
import pizzeria.order.domain.order.OrderService;
import pizzeria.order.domain.store.StoreService;
import pizzeria.order.models.DeleteModel;
import pizzeria.order.models.OrdersResponse;

/**
 * The type Order controller.
 * Responsible for handling the order endpoints
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private final transient AuthManager authManager;
    private final transient OrderService orderService;

    private final transient StoreService storeService;

    private final transient MailingService mailingService;

    /**
     * Instantiates a new Order controller with the needed authentication manager and services
     *
     * @param authManager  the authentication manager
     * @param orderService the order service
     */
    @Autowired
    public OrderController(AuthManager authManager, OrderService orderService, MailingService mailingService, StoreService storeService) {
        this.authManager = authManager;
        this.orderService = orderService;
        this.mailingService = mailingService;
        this.storeService = storeService;
    }

    /**
     * Place an order endpoint, persists the order to the database if valid
     * Includes validation of user and processes order in the order service
     *
     * @param incoming the incoming order
     * @return the response entity
     */
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order incoming) {
        try {
            //check if the order that is trying to be placed is by the user the request comes from
            //if not then we deny the operation, else we process the order (and validate everything else)
            String userId = authManager.getNetId();

            if (!userId.equals(incoming.getUserId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, "You are trying to place an order for someone else").build();
            }

            //return the order we just processed to the user
            Order processed = orderService.processOrder(incoming);

            Long storeId = processed.getStoreId();
            String recipientEmail = storeService.getEmailById(storeId);

            mailingService.sendEmail(processed.getOrderId(), recipientEmail, MailingService.ProcessType.CREATED);

            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            //return bad request with whatever validation has failed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    /**
     * Edit an order endpoint, updates the order in the database if valid
     * Includes validation of user and processes order in order service
     *
     * @param incoming the incoming order
     * @return the response entity
     */
    @PostMapping("/edit")
    public ResponseEntity<Order> editOrder(@RequestBody Order incoming) {
        try {
            //similar checking to the place order endpoint, check the user is editing his own orders
            //if not then deny, else process and validate everything else
            String userId = authManager.getNetId();

            if (!userId.equals(incoming.getUserId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, "You are trying to edit an order from someone else").build();
            }

            //return the order we just processed to the user
            Order processed = orderService.processOrder(incoming);

            Long storeId = processed.getStoreId();
            String recipientEmail = storeService.getEmailById(storeId);

            mailingService.sendEmail(processed.getOrderId(), recipientEmail, MailingService.ProcessType.EDITED);

            return ResponseEntity.status(HttpStatus.CREATED).body(processed);
        } catch (Exception e) {
            //return bad request with whatever validation has failed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    /**
     * Delete order endpoint, deletes from the database if valid request
     * Includes user validation and processes order in order service
     *
     * @param deleteModel model containing the order id we want to remove
     * @return the response entity
     */
    @DeleteMapping("/delete")
    @SuppressWarnings("PMD")
    public ResponseEntity<Order> deleteOrder(@RequestBody DeleteModel deleteModel) {
        //get the user that is trying to delete the order
        String userId = authManager.getNetId();
        //check if the user is a manager
        boolean isManager = authManager.getRole().equals("[ROLE_MANAGER]");

        Optional <Order> orderToBeDeleted = orderService.findOrder(deleteModel.getOrderId());

        if (orderToBeDeleted.isPresent()) {
            Long storeId = orderToBeDeleted.get().getStoreId();
            String recipientEmail = storeService.getEmailById(storeId);

            if (!orderService.removeOrder(deleteModel.getOrderId(), userId, isManager)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            mailingService.sendEmail(deleteModel.getOrderId(), recipientEmail, MailingService.ProcessType.DELETED);
            //validate if we can delete this order, if we can ok else bad request
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * List orders endpoint, lists all the orders belonging to a user
     *
     * @return the response entity
     */
    @GetMapping("/list")
    public ResponseEntity<OrdersResponse> listOrders() {
        String userId = authManager.getNetId();
        //get a list of the orders that belong to this user
        List<Order> orders = orderService.listOrders(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new OrdersResponse(orders));
    }

    /**
     * List all orders enpoint, only visible to manager
     * returns a list of all the orders in the system
     *
     * @return the response entity
     */
    @GetMapping("/listAll")
    public ResponseEntity<OrdersResponse> listAllOrders() {
        //get all the orders in the system
        List<Order> orders = orderService.listAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(new OrdersResponse(orders));
    }
}
