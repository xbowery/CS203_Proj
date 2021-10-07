package com.app.APICode.measure.hawker;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureHawkerController {
    private MeasureHawkerRepository measureHawkers;

    public MeasureHawkerController(MeasureHawkerRepository measureHawkers) {
        this.measureHawkers = measureHawkers;
    }

    @GetMapping("/measureHawkers")
    public List<MeasureHawker> getMeasureHawkers(){
        return measureHawkers.findAll();
    }
}
