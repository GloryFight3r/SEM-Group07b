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

@Service
public class OrderService {
    private final transient OrderRepository orderRepo;
    private final transient FoodRepository foodRepo;
    private transient FoodPriceService foodPriceService;
    private transient final CouponRepository couponRepository;

    @Autowired
    public OrderService(OrderRepository orderRepo, FoodRepository foodRepo, FoodPriceService foodPriceService,
                        CouponRepository couponRepository){
        this.orderRepo = orderRepo;
        this.foodRepo = foodRepo;
        this.foodPriceService = foodPriceService;
        this.couponRepository = couponRepository;
    }

    public Order processOrder(Order order) throws Exception {
        if (order == null)
            throw new CouldNotStoreException();

        // check if we are in 'edit mode' (the orderId in specified in the Order object)
        // then check if the order belongs to the user
        if (order.orderId != null && !order.getUserId().equals(orderRepo.findById(order.orderId).orElse(new Order()).getUserId()))
            throw new InvalidEditException();

        LocalDateTime current = LocalDateTime.now();
        if (order.getPickupTime().isBefore(current.plusMinutes(30)))
            throw new TimeInvalidException();

        GetPricesResponseModel prices = foodPriceService.getFoodPrices(order); // get prices
        if (prices == null)
            throw new FoodInvalidException();

        ArrayList<Coupon> coupons = new ArrayList<>(couponRepository.findAllById(order.couponIds));
        // this list only contains validated coupons, no need for additional checks
        order.couponIds.clear(); // clear the list, so we can send only the used one back

        if (coupons.isEmpty()) { // If coupon list is empty, just add all ingredients and recipes
            double sum = 0;
            for (Food f: order.getFoods()) {
                sum += prices.getFoodPrices().get(f.getRecipeId()).getPrice();
                for (long l: f.getBaseIngredients())
                    sum += prices.getIngredientPrices().get(l).getPrice();
                for (long l: f.getExtraIngredients())
                    sum += prices.getIngredientPrices().get(l).getPrice();
            }
            if (order.price != sum) {
                throw new PriceNotRightException();
            }
            return orderRepo.save(order);
        }

        double minPrice = Double.MAX_VALUE;
        for (Coupon c: coupons) {
            double price = c.calculatePrice(order);
            if (price < minPrice) {
                minPrice = price;
                order.couponIds.set(0, c.getId());
            }
        }
        if (order.price != minPrice)
            throw new PriceNotRightException();

        return orderRepo.save(order);
    }

    public boolean removeOrder(Long orderId, Long userId, boolean isManager) {
        if (orderId == null || userId == null)
            return false;
        if (isManager || userId.equals(orderRepo.findById(orderId).orElse(new Order()).getUserId())) {
            orderRepo.deleteById(orderId);
            return true;
        }
        return false;
    }

    public List<Order> listOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public List<Order> listAllOrders() {
        return orderRepo.findAll();
    }

    @SuppressWarnings("PMD")
    public static class PriceNotRightException extends Exception {
        @Override
        public String getMessage(){
            return "The price calculated does not match the price given";
        }
    }
    @SuppressWarnings("PMD")
    public static class TimeInvalidException extends Exception {
        @Override
        public String getMessage(){
            return "The selected pickup time is not valid.";
        }
    }

    @SuppressWarnings("PMD")
    public static class CouldNotStoreException extends Exception {
        @Override
        public String getMessage(){
            return "The order is null or it already exists in the database.";
        }
    }

    @SuppressWarnings("PMD")
    public static class FoodInvalidException extends Exception {
        @Override
        public String getMessage(){
            return "The order contains invalid recipe/ingredient ids.";
        }
    }

    @SuppressWarnings("PMD")
    public static class InvalidEditException extends Exception {
        @Override
        public String getMessage(){
            return "The order does not belong to the same user.";
        }
    }
}
