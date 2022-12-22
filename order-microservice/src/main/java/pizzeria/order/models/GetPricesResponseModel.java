package pizzeria.order.models;

import lombok.Data;

import java.util.Map;


/**
 * The type Get prices response model.
 */
@Data
public class GetPricesResponseModel {
    private Map<Long, Tuple> foodPrices;
    private Map<Long, Tuple> ingredientPrices;

    public GetPricesResponseModel(Map<Long, Tuple> f, Map<Long, Tuple> i) {
        foodPrices = f;
        ingredientPrices = i;
    }
}
