package com.app.APICode.measure;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class MeasureController {
    private MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    @GetMapping("/measures")
    public List<Measure> getMeasures(){
        return measureService.listMeasures();
    }

    @GetMapping("/measures/latest")
    public Measure getLatestMeasure(){
        return measureService.getLatestMeasure();
    }

    @GetMapping("/measures/{creationDateTime}")
    public Measure getMeasure(@PathVariable Date creationDateTime) {
        Measure measure = measureService.getMeasure(creationDateTime);

        if (measure== null) throw new MeasureNotFoundException(creationDateTime);
        return measureService.getMeasure(creationDateTime);
    }

    /**
    * @param measure
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/measures")
    public Measure addMeasure(@Valid @RequestBody Measure measure){
        Measure savedMeasure = measureService.addMeasure(measure);
        return savedMeasure;
    }

    /**
     * If there is no measure with the given creationDateTime, throw MeasureHawkerNotFoundException
     * @param creationDateTime
     * @param newMeasureInfo
     * @return the updated measure
     */
    @PutMapping("/measures/{creationDateTime}")
    public Measure updateMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime, @Valid @RequestBody Measure newMeasureInfo) {
        Measure measure = measureService.updateMeasure(creationDateTime, newMeasureInfo);
        if (measure == null) throw new MeasureNotFoundException(creationDateTime);

        return measure;
    }

    @Transactional
    @DeleteMapping("/measures/{creationDateTime}")
    public void deleteMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime) {
        try {
            measureService.deleteMeasure(creationDateTime);
        } catch (EmptyResultDataAccessException e) {
            throw new MeasureNotFoundException(creationDateTime);
        }
    }
    
}
