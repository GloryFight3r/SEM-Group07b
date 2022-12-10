package pizzeria.food.models.prices;

import lombok.Data;

import java.util.List;

@Data
public class GetPricesResponseModel {
    private List<Double> foodPrices;
    private List<Double> ingredientPrices;
}
