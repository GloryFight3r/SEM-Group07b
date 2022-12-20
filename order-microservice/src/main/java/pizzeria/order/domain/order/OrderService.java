package pizzeria.order.domain.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pizzeria.order.domain.coupon.CouponRepository;
import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.coupon.Coupon;
import pizzeria.order.domain.food.FoodPriceService;
import pizzeria.order.domain.food.FoodRepository;
import pizzeria.order.models.GetPricesResponseModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Order service
 * Handles interaction with the endpoints and repositories, contains most verification logic
 */
@Service
public class OrderService {
    private final transient OrderRepository orderRepo;
    private final transient FoodRepository foodRepo;
    private final transient FoodPriceService foodPriceService;
    private transient final CouponRepository couponRepository;
    private final ClockWrapper clockWrapper;

    /**
     * Instantiates a new Order service with the respective repositories and services
     *
     * @param orderRepo        the order repository
     * @param foodRepo         the food repo repository
     * @param foodPriceService the food price service
     * @param couponRepository the coupon repository
     */
    @Autowired
    public OrderService(OrderRepository orderRepo, FoodRepository foodRepo, FoodPriceService foodPriceService,
                        CouponRepository couponRepository){
        this.orderRepo = orderRepo;
        this.foodRepo = foodRepo;
        this.foodPriceService = foodPriceService;
        this.couponRepository = couponRepository;
        this.clockWrapper = new ClockWrapper();
    }

    /**
     * Process an order
     * Includes validation of user, of time, of price, of foods and of coupons
     *
     * @param order the order to be processed
     * @return the order, after processing
     * @throws PriceNotRightException something went wrong with the price calculation on the user end
     * @throws TimeInvalidException the selected pickup time does not pass the criteria
     * @throws CouldNotStoreException there was an error with the order passed so it cannot be stored in the database
     * @throws FoodInvalidException there are invalid or non-existing foods in the order placed
     * @throws InvalidEditException exclusively when editing an order, the order does not belong to the user
     *
     */
    @SuppressWarnings("PMD")
    public Order processOrder(Order order) throws Exception {
        if (order == null)
            throw new CouldNotStoreException();

        // check if we are in 'edit mode' (the orderId is specified in the Order object)
        // then check if the order belongs to the user
        //when we find by id we return an optional, if for some reason this optional does not exist return new order, which has null fields for non-primitives
        //essentially check if the order is in the repo and belongs to the person trying to edit
        if (order.orderId != null && !order.getUserId().equals(orderRepo.findById(order.orderId).orElse(new Order()).getUserId()))
            throw new InvalidEditException();

        //check if the selected pickup time is 30 minutes or more in the future
        LocalDateTime current = clockWrapper.getNow();
        if (order.getPickupTime().isBefore(current.plusMinutes(30)))
            throw new TimeInvalidException();

        GetPricesResponseModel prices = foodPriceService.getFoodPrices(order); // get prices
        if (prices == null)
            //some food does not exist or something else went wrong in the food ms communication
            throw new FoodInvalidException();

        ArrayList<Coupon> coupons = new ArrayList<>(couponRepository.findAllById(order.couponIds));
        // this list only contains validated coupons, no need for additional checks
        order.couponIds.clear(); // clear the list, so we can send only the used one back

        //get the base price of the order
        double sum = 0.0;
        for (Food f: order.getFoods()) {
            sum += prices.getFoodPrices().get(f.getRecipeId()).getPrice();
            for (long l: f.getBaseIngredients())
                sum += prices.getIngredientPrices().get(l).getPrice();
            for (long l: f.getExtraIngredients())
                sum += prices.getIngredientPrices().get(l).getPrice();
        }

        if (coupons.isEmpty()) { // If coupon list is empty, just add all ingredients and recipes
            if (Double.compare(order.price, sum) != 0) {
                throw new PriceNotRightException();
            }
            return orderRepo.save(order);
        }

        double minPrice = Double.MAX_VALUE;
        for (Coupon c: coupons) {
            //iterate over the list of valid coupons
            double price = c.calculatePrice(order, prices, sum);
            if (Double.compare(price, minPrice) < 0) {
                minPrice = price;
                //set the first element in the coupon ids to the coupon used
                order.couponIds.set(0, c.getId());
            }
        }

        if (Double.compare(order.price, minPrice) != 0)
            throw new PriceNotRightException();

        return orderRepo.save(order);
    }

    /**
     * Remove an order
     * Includes user validation or check if we have a manager requesting this, also check time constraint
     *
     * @param orderId   the order id
     * @param userId    the user id
     * @param isManager the is manager
     * @return the boolean
     */
    public boolean removeOrder(Long orderId, String userId, boolean isManager) {
        if (orderId == null || userId == null)
            return false;
        //check if the order exists
        Order toDelete = orderRepo.findById(orderId).orElse(null);
        if (toDelete == null){
            return false;
        }
        //check if the selected pickup time is 30 minutes or more in the future
        LocalDateTime current = clockWrapper.getNow();
        if (toDelete.getPickupTime().isBefore(current.plusMinutes(30)))
            return false;

        //check if we have a manager or the order belongs to the user
        //we already check above that the order exists, so now we only check if the user ids match
        if (isManager || userId.equals(toDelete.getUserId())) {
            orderRepo.deleteById(orderId);
            return true;
        }
        return false;
    }

    /**
     * List orders, finds all the orders matching the user id in the repo
     *
     * @param userId the user id to find orders for
     * @return the list of orders
     */
    public List<Order> listOrders(String userId) {
        return orderRepo.findByUserId(userId);
    }

    /**
     * List all orders, gets all the orders from the database
     *
     * @return the list of orders
     */
    public List<Order> listAllOrders() {
        return orderRepo.findAll();
    }

    /**
     * The type Price not right exception.
     */
    @SuppressWarnings("PMD")
    public static class PriceNotRightException extends Exception {
        @Override
        public String getMessage(){
            return "The price calculated does not match the price given";
        }
    }

    /**
     * The type Time invalid exception.
     */
    @SuppressWarnings("PMD")
    public static class TimeInvalidException extends Exception {
        @Override
        public String getMessage(){
            return "The selected pickup time is not valid.";
        }
    }

    /**
     * The type Could not store exception.
     */
    @SuppressWarnings("PMD")
    public static class CouldNotStoreException extends Exception {
        @Override
        public String getMessage(){
            return "The order is null or it already exists in the database.";
        }
    }

    /**
     * The type Food invalid exception.
     */
    @SuppressWarnings("PMD")
    public static class FoodInvalidException extends Exception {
        @Override
        public String getMessage(){
            return "The order contains invalid recipe/ingredient ids.";
        }
    }

    /**
     * The type Invalid edit exception.
     */
    @SuppressWarnings("PMD")
    public static class InvalidEditException extends Exception {
        @Override
        public String getMessage(){
            return "The order does not belong to the same user.";
        }
    }
}
