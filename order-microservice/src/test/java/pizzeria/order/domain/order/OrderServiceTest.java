package pizzeria.order.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pizzeria.order.domain.coupon.*;
import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.food.FoodPriceService;
import pizzeria.order.domain.store.Store;
import pizzeria.order.domain.store.StoreService;
import pizzeria.order.models.GetPricesResponseModel;
import pizzeria.order.models.Tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "restTemplateProfile", "mockPriceService"})
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
        Food f1 = new Food();
        f1.setRecipeId(1L);
        f1.setBaseIngredients(List.of(1L));
        f1.setExtraIngredients(List.of(0L, 2L));

        Food f2 = new Food();
        f2.setRecipeId(0L);
        f2.setBaseIngredients(List.of());
        f2.setExtraIngredients(List.of());

        Food f3 = new Food();
        f3.setRecipeId(0L);
        f3.setBaseIngredients(List.of());
        f3.setExtraIngredients(List.of());

        ArrayList<Food> foodList = new ArrayList<Food>(List.of(f1, f2, f3));
        ArrayList<Food> invalidFoodList = new ArrayList<Food>(List.of(
                new Food(99L, 99L, 99L, List.of(99L), List.of(99L))
        ));

        Store st = storeService.addStore(new Store("NL-2624ME", "baa@gmail.com"));

        HashMap<Long, Tuple> recipePrices = new HashMap<>();
        recipePrices.put(0L, new Tuple(11, "Margherita"));
        recipePrices.put(1L, new Tuple(12, "Peperoni"));
        HashMap<Long, Tuple> ingredientPrices = new HashMap<>();
        ingredientPrices.put(0L, new Tuple(2, "Pineapple"));
        ingredientPrices.put(1L, new Tuple(1, "Salami"));
        ingredientPrices.put(2L, new Tuple(1, "Mushrooms"));
        pricesResponseModel = new GetPricesResponseModel(recipePrices, ingredientPrices);

        coupon_percentage_repository.save(new PercentageCoupon("percentage", 0.5));
        coupon_2for1_repository.save( new TwoForOneCoupon("2for1"));

        order_invalidTime = new Order(null, foodList, 1L, "uid", LocalDateTime.now(), 36, new ArrayList<String>(List.of()));
        order_invalidFood = new Order(null, invalidFoodList, 1L, "uid", LocalDateTime.now().plusHours(1), 36, new ArrayList<String>(List.of()));
        order_invalidCoupons = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 37, new ArrayList<String>(List.of("invalid")));
        order_invalidPrice = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 26, new ArrayList<String>(List.of("percentage", "2for1")));
        order_valid = new Order(null, foodList, 1L, "uid", LocalDateTime.now().plusHours(1), 18.5, new ArrayList<String>(List.of("percentage", "2for1", "invalid")));
    }

    @Test
    void testListValidOrder() throws Exception {
        // assert 0 orders
        assertTrue(orderService.listAllOrders().isEmpty());

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        orderService.processOrder(order_valid);
        assertEquals(orderService.listAllOrders().size(), 1);
    }

    @Test
    void testListInvalidCoupons() throws Exception {
        // assert 0 orders
        assertTrue(orderService.listAllOrders().isEmpty());

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        orderService.processOrder(order_invalidCoupons);

        List<Order> orders = orderService.listAllOrders();
        assertEquals(orders.size(), 1);
    }

    @Test
    void testProcessOrder_invalidTime() throws Exception {
        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);
        assertThatThrownBy(() -> {
            orderService.processOrder(order_invalidTime);
        }).isInstanceOf(OrderService.TimeInvalidException.class);
    }

    @Test
    void testProcessOrder_orderIsNull() throws Exception {
        assertThatThrownBy(() -> {
            orderService.processOrder(null);
        }).isInstanceOf(OrderService.CouldNotStoreException.class);
    }

    @Test
    void testEditOrder_orderNotNullButBelongsToDifferentUser() throws Exception {
        Order currentOrder = orderRepository.save(order_valid);

        Order editOrder = new Order();
        editOrder.setOrderId(currentOrder.orderId);
        editOrder.setFoods(currentOrder.getFoods());
        editOrder.setPrice(currentOrder.getPrice());
        editOrder.setCouponIds(currentOrder.getCouponIds());
        editOrder.setPickupTime(currentOrder.getPickupTime());
        editOrder.setUserId("differentId");

        assertThatThrownBy(() -> {
            orderService.processOrder(editOrder);
        }).isInstanceOf(OrderService.InvalidEditException.class);
    }

    @Test
    void testPlaceOrder_priceIsNull() throws Exception {
        Order editOrder = new Order();
        when(foodPriceService.getFoodPrices(any())).thenReturn(null);

        editOrder.setFoods(order_valid.getFoods());
        editOrder.setStoreId(order_valid.getStoreId());
        editOrder.setCouponIds(order_valid.getCouponIds());
        editOrder.setPickupTime(order_valid.getPickupTime());
        editOrder.setUserId("differentId");

        assertThatThrownBy(() -> {
            orderService.processOrder(editOrder);
        }).isInstanceOf(OrderService.FoodInvalidException.class);
    }
}
