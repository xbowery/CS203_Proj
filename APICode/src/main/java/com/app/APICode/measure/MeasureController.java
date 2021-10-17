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

    /**
     * List all measures in the system
     * @return list of all measures
     */
    @GetMapping("/measures")
    public List<Measure> getMeasures(){
        return measureService.listMeasures();
    }

    /**
     * List the latest measures (by DateTime) in the system
     * @return list of latest measures
     */
    @GetMapping("/measures/latest")
    public Measure getLatestMeasure(){
        return measureService.getLatestMeasure();
    }

    /**
     * Search for the measure given the creationDateTime
     * If there is no measure with the given creationDateTime, throw a MeasureNotFoundException
     * @param creationDateTime Date object containing the date to be searched
     * @return measure with the given creationDateTime
     */
    @GetMapping("/measures/{creationDateTime}")
    public Measure getMeasure(@PathVariable Date creationDateTime) {
        Measure measure = measureService.getMeasure(creationDateTime);

        if (measure== null) throw new MeasureNotFoundException(creationDateTime);
        return measureService.getMeasure(creationDateTime);
    }

    /**
    * Add a new measure with POST request to "/measures"
    * @param measure
    * @return the newly added measure
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/measures")
    public Measure addMeasure(@Valid @RequestBody Measure measure){
        Measure savedMeasure = measureService.addMeasure(measure);
        return savedMeasure;
    }

    /**
     * Update the info of a measure
     * If there is no measure with the given creationDateTime, throw MeasureHawkerNotFoundException
     * @param creationDateTime the creationDateTime of the measure
     * @param newMeasureInfo a Measure object containing the new measure info to be updated
     * @return the updated Measure object
     */
    @PutMapping("/measures/{creationDateTime}")
    public Measure updateMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime, @Valid @RequestBody Measure newMeasureInfo) {
        Measure measure = measureService.updateMeasure(creationDateTime, newMeasureInfo);
        if (measure == null) throw new MeasureNotFoundException(creationDateTime);

        return measure;
    }

    /**
     * Remove a measure with the DELETE request to "/measures/{creationDateTime}"
     * If there is no measure with the given creationDateTime, throw a MeasureNotFoundException
     * @param creationDateTime the creationDateTime of the measure
     */
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
