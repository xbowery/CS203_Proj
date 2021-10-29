package com.app.APICode.measure;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeasureController {
    private MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    /**
     * List all measures in the system
     * 
     * @return list of all measures
     */
    @GetMapping("/measures")
    public List<Measure> getMeasures() {
        return measureService.listMeasures();
    }

    /**
     * Search for the measure given the creationDateTime If there is no measure with
     * the given creationDateTime, throw a MeasureNotFoundException
     * 
     * @param measureType the type of the measure
     * @return measure with the given type
     */
    @GetMapping("/measures/{measureType}")
    public Measure getMeasure(@PathVariable @NotNull String measureType) {
        return measureService.getMeasure(measureType);
    }

    /**
     * Add a new measure with POST request to "/measures"
     * 
     * @param measure
     * @return the newly added measure
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/measures")
    public Measure addMeasure(@Valid @RequestBody Measure measure) {
        return measureService.addMeasure(measure);
    }

    /**
     * Update the info of a measure If there is no measure with the given
     * creationDateTime, throw MeasureNotFoundException
     * 
     * @param updatedMeasure   a Measure object containing the new measure info to
     *                         be updated
     * @return the updated Measure object
     */
    @PutMapping("/measures")
    public Measure updateMeasure(@Valid @RequestBody Measure updatedMeasure) {
        return measureService.updateMeasure(updatedMeasure);
    }

    /**
     * Remove a measure with the DELETE request to "/measures/{measureType}" If
     * there is no measure with the given creationDateTime, throw a
     * MeasureNotFoundException
     * 
     * @param measureType the type of the measure
     */
    @Transactional
    @DeleteMapping("/measures/{measureType}")
    public void deleteMeasure(@PathVariable @NotNull String measureType) {
        measureService.deleteMeasure(measureType);
    }

}
