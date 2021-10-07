package com.app.APICode.measure.restaurant;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureRestaurantController {
    private MeasureRestaurantRepository measureRestaurant;

    public MeasureRestaurantController(MeasureRestaurantRepository measureRestaurant) {
        this.measureRestaurant = measureRestaurant;
    }

    @GetMapping("/measureRestaurant")
    public List<MeasureRestaurant> getMeasureRestaurant() {
        return measureRestaurant.findAll();
    }
}
