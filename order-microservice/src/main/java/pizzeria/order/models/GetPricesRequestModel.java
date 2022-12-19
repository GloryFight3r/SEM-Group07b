package pizzeria.order.models;

import lombok.Data;

import java.util.List;

@Data
public class GetPricesRequestModel {
    private List<Long> foodIds;
    private List<Long> ingredientIds;

    public GetPricesRequestModel(List<Long>foodIds, List<Long>ingredientIds) {
        this.foodIds = foodIds;
        this.ingredientIds = ingredientIds;
    }

}
