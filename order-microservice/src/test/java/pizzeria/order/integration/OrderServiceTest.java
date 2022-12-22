package pizzeria.order.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.order.domain.coupon.*;
import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.food.FoodPriceService;
import pizzeria.order.domain.order.Order;
import pizzeria.order.domain.order.OrderRepository;
import pizzeria.order.domain.order.OrderService;
import pizzeria.order.domain.store.Store;
import pizzeria.order.domain.store.StoreService;
import pizzeria.order.models.GetPricesResponseModel;
import pizzeria.order.models.Tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockStoreService", "mockFoodPriceService", "mockCouponRepository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderServiceTest {
    @Autowired
    private transient OrderService orderService;
    @Autowired
    private transient OrderRepository orderRepository;
    @Autowired
    private transient StoreService storeService;
    @Autowired
    private transient FoodPriceService foodPriceService;

    @Autowired
    private transient Coupon_2for1_Repository coupon_2for1_repository;
    @Autowired
    private transient Coupon_percentage_Repository coupon_percentage_repository;

    private transient Order order_invalidTime;
    private transient Order order_invalidFood; // references non-existent recipes and ingredients
    private transient Order order_invalidCoupons; // it's still a valid order, but all the coupons are invalid
    private transient Order order_invalidPrice; // price does not match correct price
    private transient Order order_valid; // perfect case
    private transient GetPricesResponseModel pricesResponseModel;

    @BeforeEach
    void beforeEach() throws Exception {
        // contains one peperoni with salami as base and extra pineapple and mushrooms, and 2 basic margheritas
        // normal price = 36.0, percentage discount price = 18.0, 2for1 discount price = 26.0
        ArrayList<Food> foodList = new ArrayList<Food>(List.of(
                new Food(0L, 1L, 0L, List.of(1L), List.of(0L, 2L)),
                new Food(1L, 0L, 0L, List.of(), List.of()),
                new Food(2L, 0L, 0L, List.of(), List.of())
        ));
        ArrayList<Food> invalidFoodList = new ArrayList<Food>(List.of(
                new Food(99L, 99L, 99L, List.of(99L), List.of(99L))
        ));

        Store st = storeService.addStore(new Store("NL-2624ME", "ba@abv.bg"));

        HashMap<Long, Tuple> recipePrices = new HashMap<>();
        recipePrices.put(0L, new Tuple(11, "Margherita"));
        recipePrices.put(1L, new Tuple(12, "Peperoni"));
        HashMap<Long, Tuple> ingredientPrices = new HashMap<>();
        ingredientPrices.put(0L, new Tuple(2, "Pineapple"));
        ingredientPrices.put(1L, new Tuple(1, "Salami"));
        ingredientPrices.put(2L, new Tuple(1, "Mushrooms"));
        pricesResponseModel = new GetPricesResponseModel(recipePrices, ingredientPrices);

      //  when(storeService.existsById(any())).thenReturn(true);

       // when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        //System.out.println("DASDAS" + st.getId());

        coupon_percentage_repository.save(new PercentageCoupon("percentage", 0.5));
        coupon_2for1_repository.save( new TwoForOneCoupon("2for1"));

        order_invalidTime = new Order(null, foodList, 1L, "uid", LocalDateTime.now(), 36, new ArrayList<String>(List.of()));
        order_invalidFood = new Order(null, invalidFoodList, 1L, "uid", LocalDateTime.now().plusHours(1), 36, new ArrayList<String>(List.of()));
        order_invalidCoupons = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 36, new ArrayList<String>(List.of("invalid")));
        order_invalidPrice = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 26, new ArrayList<String>(List.of("percentage", "2for1")));
        order_valid = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 18, new ArrayList<String>(List.of("percentage", "2for1", "invalid")));
    }

    //@Test
    void testListAllOrders() throws Exception {
        // assert 0 orders
        assertTrue(orderService.listAllOrders().isEmpty());

        // assert with 2 orders
        orderService.processOrder(order_valid);
       // assertEquals(orderService.listAllOrders().size(), 1);
       // orderService.processOrder(order_invalidCoupons);

        //List<Order> orders = orderService.listAllOrders();
        //assertEquals(orders.size(), 2);
        //assertTrue(orders.contains(order_valid));
       // assertTrue(orders.contains(order_invalidCoupons));

    }

    //@Test
    void testProcessOrder() throws Exception {
        assertThrows(OrderService.TimeInvalidException.class, (Executable) orderService.processOrder(order_invalidTime));
    }
}
