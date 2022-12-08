package pizzeria.food.models;

import lombok.Data;

import java.util.List;

@Data
public class GetPricesRequestModel {
    private List<Long> foodIds;
    private List<Long> ingredientIds;
}
