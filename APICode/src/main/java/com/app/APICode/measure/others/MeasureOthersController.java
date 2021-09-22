package com.app.APICode.measure.others;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureOthersController {
    private MeasureOthersRepository measureOthers;

    public MeasureOthersController(MeasureOthersRepository measureOthers) {
        this.measureOthers = measureOthers;
    }

    @GetMapping("/measureHawkers")
    public List<MeasureOthers> getMeasureOthers(){
        return measureOthers.findAll();
    }
    
}
