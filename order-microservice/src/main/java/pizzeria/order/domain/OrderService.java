package pizzeria.order.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final transient OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }

    public Order saveOrder(Order order) throws Exception{
        if (order == null || orderRepo.existsById(order.getId())){
            return null;
        }
        //check if the prices match, applies the cheapest coupon
        if (order.getPrice() != order.calculatePrice()){
            throw new PriceNotRightException();
        }
        //check the pickup time is not in the past
        LocalDateTime currentTime = LocalDateTime.now();
        if (order.getPickupTime().isBefore(currentTime)){
            throw new TimeIsPastException();
        }

        return orderRepo.save(order);
    }

    public static class PriceNotRightException extends Exception{
        @Override
        public String getMessage(){
            return "The price calculated does not match the price given";
        }
    }

    public static class TimeIsPastException extends Exception{
        @Override
        public String getMessage(){
            return "The selected pickup time is in the past";
        }
    }
}
