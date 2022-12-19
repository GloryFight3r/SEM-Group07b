package pizzeria.order.domain.coupon;

import pizzeria.order.domain.food.Food;
import pizzeria.order.domain.order.Order;
import pizzeria.order.models.GetPricesResponseModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Two for one coupon.
 */
public class TwoForOneCoupon extends Coupon {

    /**
     * Empty constructor for database purposes
     */
    public TwoForOneCoupon() {
        super();
    }


    @Override
    public double calculatePrice(Order order, GetPricesResponseModel prices, double basePrice) {
        //make a hashmap that keeps track of every food in the list with its occurrences
        Map<Long, Integer> foodMap = new HashMap<>();
        for (Food f : order.getFoods()){
            //if this recipe is already in the choices, add an occurrence else put it with 1 occurrence
            if (!foodMap.containsKey(f.getRecipeId())){
                foodMap.put(f.getRecipeId(), 1);
            }else {
                foodMap.put(f.getRecipeId(), foodMap.get(f.getRecipeId()) + 1);
            }
        }

        //now every time we have 2 times an item we only charge it once
        //so essentially we subtract from the price (occurrences)/2 - 1 per recipe
        //this coupon could be made a little more interesting if we add a recipe id (like margherita pizza)
        double reduction = 0.0;
        for (Long key : foodMap.keySet()){
            int reduced_times = (foodMap.get(key) / 2 - 1);
            reduction += (double) reduced_times * prices.getIngredientPrices().get(key).getPrice();
        }

        //return the base price - the reduction
        return basePrice - reduction;
    }
}
