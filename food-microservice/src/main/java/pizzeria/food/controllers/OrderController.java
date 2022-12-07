package pizzeria.food.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pizzeria.food.domain.FoodService;

@RestController
@RequestMapping("/food")
public class OrderController {

    private final transient FoodService foodService;

    @Autowired
    public OrderController(FoodService foodService){
        this.foodService = foodService;
    }
}
