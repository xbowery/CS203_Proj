package com.app.APICode.measure.hawker;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
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
public class MeasureHawkerController {
    private MeasureHawkerService measureHawkerService;

    public MeasureHawkerController(MeasureHawkerService measureHawkerService) {
        this.measureHawkerService = measureHawkerService;
    }

    @GetMapping("/measureHawkers")
    public List<MeasureHawker> getMeasureHawkers(){
        return measureHawkerService.listMeasureHawkers();
    }

    @GetMapping("/measureHawkers/{creationDateTime}")
    public MeasureHawker getMeasureHawker(@PathVariable Date creationDateTime) {
        MeasureHawker measureHawker = measureHawkerService.getMeasureHawker(creationDateTime);

        if (measureHawker== null) throw new MeasureHawkerNotFoundException(creationDateTime);
        return measureHawkerService.getMeasureHawker(creationDateTime);
    }

    /**
    * @param measureHawker
    * @return
    */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/measureHawkers")
    public MeasureHawker addMeasureHawker(@Valid @RequestBody MeasureHawker measureHawker){
        MeasureHawker savedMeasureHawker = measureHawkerService.addMeasureHawker(measureHawker);
        return savedMeasureHawker;
    }

    /**
     * If there is no measure with the given creationDateTime, throw MeasureHawkerNotFoundException
     * @param creationDateTime
     * @param newMeasureHawkerInfo
     * @return the updated measure
     */
    @PutMapping("/measureHawkers/{creationDateTime}")
    public MeasureHawker updateMeasureHawker(@PathVariable Date creationDateTime, @Valid @RequestBody MeasureHawker newMeasureHawkerInfo) {
        MeasureHawker measureHawker = measureHawkerService.updateMeasureHawker(creationDateTime, newMeasureHawkerInfo);
        if (measureHawker == null) throw new MeasureHawkerNotFoundException(creationDateTime);

        return measureHawker;
    }

    @Transactional
    @DeleteMapping("/measureHawkers/{creationDateTime}")
    public void deleteMeasureHawker(@PathVariable Date creationDateTime) {
        try {
            measureHawkerService.deleteMeasureHawker(creationDateTime);
        } catch (EmptyResultDataAccessException e) {
            throw new MeasureHawkerNotFoundException(creationDateTime);
        }
    }
    
}
