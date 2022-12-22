package pizzeria.order.profiles;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pizzeria.order.domain.food.FoodPriceService;

@Profile("mockFoodPriceService")
@Configuration
public class MockFoodPriceServiceProfile {
    @Bean
    @Primary
    public FoodPriceService getMockFoodPriceService() {
        return Mockito.mock(FoodPriceService.class);
    }
}
