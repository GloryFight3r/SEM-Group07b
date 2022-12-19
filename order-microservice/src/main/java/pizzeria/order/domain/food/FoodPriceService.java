package pizzeria.order.domain.food;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pizzeria.order.domain.order.Order;
import pizzeria.order.models.GetPricesResponseModel;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodPriceService {
    private final transient RestTemplate restTemplate;

    public FoodPriceService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public GetPricesResponseModel getFoodPrices(Order order) {
        List<Long> ingredients = new ArrayList<>();
        for (Food f: order.getFoods())
            ingredients.addAll(f.getExtraIngredients());
        List<Long> recipes = order.getFoods().stream()
                .map(Food::getRecipeId).collect(Collectors.toList());

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create headers
        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("foodIds", recipes);
        map.put("ingredientIds", ingredients);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<GetPricesResponseModel> response =
                this.restTemplate.postForEntity("http://localhost:8084/price/ids", entity, GetPricesResponseModel.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

}
