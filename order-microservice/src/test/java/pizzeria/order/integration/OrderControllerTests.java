package pizzeria.order.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import pizzeria.order.authentication.AuthManager;
import pizzeria.order.authentication.JwtTokenVerifier;
import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.food.FoodPriceService;
import pizzeria.order.domain.food.FoodRepository;
import pizzeria.order.domain.mailing.MailingService;
import pizzeria.order.domain.order.ClockWrapper;
import pizzeria.order.domain.order.Order;
import pizzeria.order.domain.order.OrderRepository;
import pizzeria.order.domain.store.Store;
import pizzeria.order.domain.store.StoreService;
import pizzeria.order.integration.utils.JsonUtil;
import pizzeria.order.models.GetPricesResponseModel;
import pizzeria.order.models.OrderPlaceModel;
import pizzeria.order.models.Tuple;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager", "restTemplateProfile", "mockMailService", "mockPriceService", "clockWrapper"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient AuthManager mockAuthManager;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient FoodRepository foodRepository;

    @Autowired
    private transient OrderRepository orderRepository;

    @Autowired
    private transient MailingService mailingService;

    @Autowired
    private transient StoreService storeService;

    @Autowired
    private transient RestTemplate restTemplate;

    @Autowired
    private transient FoodPriceService foodPriceService;

    @Autowired
    private transient ClockWrapper clockWrapper;


    @BeforeEach
    public void init() {
        when(mockAuthManager.getNetId()).thenReturn("Mocked Id");
        when(mockAuthManager.getRole()).thenReturn("ROLE_MANAGER");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getNetIdFromToken(anyString())).thenReturn("Mocked Id");
        when(mockJwtTokenVerifier.getRoleFromToken(anyString())).thenReturn(List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));
        when(clockWrapper.getNow()).thenReturn(LocalDateTime.of(2022, Month.JANUARY, 3, 14, 31, 1));

        try {
            storeService.addStore(new Store("NL-2624ME", "bor@abv.bg"));
        } catch (Exception e) {
            System.out.println("ERROR");
        }

    }

    @Test
    void placeOrder_worksCorrectly() throws Exception {
        Food firstFood = new Food();
        firstFood.setBaseIngredients(List.of(1L));
        firstFood.setExtraIngredients(List.of(4L));
        firstFood.setRecipeId(2L);

        OrderPlaceModel order = new OrderPlaceModel();
        order.setUserId("Mocked Id");
        order.setCouponIds(List.of());
        order.setFoods(List.of(firstFood));
        order.setPickupTime(LocalDateTime.of(2023, Month.JANUARY, 3, 2, 1, 3));
        order.setPrice(127.8);
        order.setStoreId(1L);

        Map<Long, Tuple> foodPrices = new HashMap<>();
        Map<Long, Tuple> ingredientPrices = new HashMap<>();

        foodPrices.put(2L, new Tuple(100.0, "MockName1"));
        ingredientPrices.put(1L, new Tuple(13.5, "MockName2"));
        ingredientPrices.put(4L, new Tuple(14.3, "MockName3"));

        GetPricesResponseModel pricesResponseModel = new GetPricesResponseModel();
        pricesResponseModel.setIngredientPrices(ingredientPrices);
        pricesResponseModel.setFoodPrices(foodPrices);

        String serializedString = JsonUtil.serialize(order);

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedString)
                .header("Authorization", "Bearer MockedToken"));

        MvcResult response = resultActions.andExpect(status().isCreated()).andReturn();

        System.out.println(response.getResponse().toString());

        Order actualOrder = JsonUtil.deserialize(response.getResponse().getContentAsString(), Order.class);

        assertThat(actualOrder.getStoreId()).isEqualTo(order.getStoreId());
        assertThat(actualOrder.getUserId()).isEqualTo(order.getUserId());
        assertThat(actualOrder.getPickupTime()).isEqualTo(order.getPickupTime());
        assertThat(actualOrder.getCouponIds()).containsExactlyInAnyOrderElementsOf(order.getCouponIds());
        for (Food currentFood : actualOrder.getFoods()) {
            assertThat(currentFood.getRecipeId()).isEqualTo(foodRepository.findById(currentFood.getId()).get().getRecipeId());
            assertThat(currentFood.getBaseIngredients()).containsExactlyInAnyOrderElementsOf(foodRepository.findById(currentFood.getId()).get().getBaseIngredients());
            assertThat(currentFood.getExtraIngredients()).containsExactlyInAnyOrderElementsOf(foodRepository.findById(currentFood.getId()).get().getExtraIngredients());
        }
    }

    @Test
    void placeOrder_invalidStore() throws Exception {
        Food firstFood = new Food();
        firstFood.setBaseIngredients(List.of(1L));
        firstFood.setExtraIngredients(List.of(4L));
        firstFood.setRecipeId(2L);

        OrderPlaceModel order = new OrderPlaceModel();
        order.setUserId("Mocked Id");
        order.setCouponIds(List.of());
        order.setFoods(List.of(firstFood));
        order.setPickupTime(LocalDateTime.of(2023, Month.JANUARY, 3, 2, 1, 3));
        order.setPrice(127.8);
        order.setStoreId(2L);

        Map<Long, Tuple> foodPrices = new HashMap<>();
        Map<Long, Tuple> ingredientPrices = new HashMap<>();

        foodPrices.put(2L, new Tuple(100.0, "MockName1"));
        ingredientPrices.put(1L, new Tuple(13.5, "MockName2"));
        ingredientPrices.put(4L, new Tuple(14.3, "MockName3"));

        GetPricesResponseModel pricesResponseModel = new GetPricesResponseModel();
        pricesResponseModel.setIngredientPrices(ingredientPrices);
        pricesResponseModel.setFoodPrices(foodPrices);

        String serializedString = JsonUtil.serialize(order);

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedString)
                .header("Authorization", "Bearer MockedToken"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void placeOrder_userIsDifferent() throws Exception {
        Food firstFood = new Food();
        firstFood.setBaseIngredients(List.of(1L));
        firstFood.setExtraIngredients(List.of(4L));
        firstFood.setRecipeId(2L);

        OrderPlaceModel order = new OrderPlaceModel();
        order.setUserId("Mocked Id2");
        order.setCouponIds(List.of());
        order.setFoods(List.of(firstFood));
        order.setPickupTime(LocalDateTime.of(2023, Month.JANUARY, 3, 2, 1, 3));
        order.setPrice(127.8);
        order.setStoreId(1L);

        Map<Long, Tuple> foodPrices = new HashMap<>();
        Map<Long, Tuple> ingredientPrices = new HashMap<>();

        foodPrices.put(2L, new Tuple(100.0, "MockName1"));
        ingredientPrices.put(1L, new Tuple(13.5, "MockName2"));
        ingredientPrices.put(4L, new Tuple(14.3, "MockName3"));

        GetPricesResponseModel pricesResponseModel = new GetPricesResponseModel();
        pricesResponseModel.setIngredientPrices(ingredientPrices);
        pricesResponseModel.setFoodPrices(foodPrices);

        String serializedString = JsonUtil.serialize(order);

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedString)
                .header("Authorization", "Bearer MockedToken"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void placeOrder_sumIsDifferent() throws Exception {
        Food firstFood = new Food();
        firstFood.setBaseIngredients(List.of(1L));
        firstFood.setExtraIngredients(List.of(4L));
        firstFood.setRecipeId(2L);

        OrderPlaceModel order = new OrderPlaceModel();
        order.setUserId("Mocked Id");
        order.setCouponIds(List.of());
        order.setFoods(List.of(firstFood));
        order.setPickupTime(LocalDateTime.of(2023, Month.JANUARY, 3, 2, 1, 3));
        order.setPrice(127.9);
        order.setStoreId(1L);

        Map<Long, Tuple> foodPrices = new HashMap<>();
        Map<Long, Tuple> ingredientPrices = new HashMap<>();

        foodPrices.put(2L, new Tuple(100.0, "MockName1"));
        ingredientPrices.put(1L, new Tuple(13.5, "MockName2"));
        ingredientPrices.put(4L, new Tuple(14.3, "MockName3"));

        GetPricesResponseModel pricesResponseModel = new GetPricesResponseModel();
        pricesResponseModel.setIngredientPrices(ingredientPrices);
        pricesResponseModel.setFoodPrices(foodPrices);

        String serializedString = JsonUtil.serialize(order);

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedString)
                .header("Authorization", "Bearer MockedToken"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void placeOrder_timeToChangeHasExpired() throws Exception {
        Food firstFood = new Food();
        firstFood.setBaseIngredients(List.of(1L));
        firstFood.setExtraIngredients(List.of(4L));
        firstFood.setRecipeId(2L);

        OrderPlaceModel order = new OrderPlaceModel();
        order.setUserId("Mocked Id");
        order.setCouponIds(List.of());
        order.setFoods(List.of(firstFood));
        order.setPickupTime(LocalDateTime.of(2023, Month.JANUARY, 3, 14, 0, 0));
        order.setPrice(127.8);
        order.setStoreId(1L);

        when(clockWrapper.getNow()).thenReturn(LocalDateTime.of(2023, Month.JANUARY, 3, 14, 31, 1));

        Map<Long, Tuple> foodPrices = new HashMap<>();
        Map<Long, Tuple> ingredientPrices = new HashMap<>();

        foodPrices.put(2L, new Tuple(100.0, "MockName1"));
        ingredientPrices.put(1L, new Tuple(13.5, "MockName2"));
        ingredientPrices.put(4L, new Tuple(14.3, "MockName3"));

        GetPricesResponseModel pricesResponseModel = new GetPricesResponseModel();
        pricesResponseModel.setIngredientPrices(ingredientPrices);
        pricesResponseModel.setFoodPrices(foodPrices);

        String serializedString = JsonUtil.serialize(order);

        when(foodPriceService.getFoodPrices(any())).thenReturn(pricesResponseModel);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/order/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedString)
                .header("Authorization", "Bearer MockedToken"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void editOrder() {

    }

    @Test
    void deleteOrder() {
    }

    @Test
    void listOrders() {
    }

    @Test
    void listAllOrders() {
    }
}
