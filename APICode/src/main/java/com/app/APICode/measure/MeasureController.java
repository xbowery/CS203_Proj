
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
public class MeasureController {
    private MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    @GetMapping("/measures")
    public List<Measure> getMeasures(){
        return measureService.listMeasures();
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
    public Measure updateMeasure(@PathVariable Date creationDateTime, @Valid @RequestBody Measure newMeasureInfo) {
        Measure measure = measureService.updateMeasure(creationDateTime, newMeasureInfo);
        if (measure == null) throw new MeasureNotFoundException(creationDateTime);

        return measure;
    }

    @Transactional
    @DeleteMapping("/measures/{creationDateTime}")
    public void deleteMeasure(@PathVariable Date creationDateTime) {
        try {
            measureService.deleteMeasure(creationDateTime);
        } catch (EmptyResultDataAccessException e) {
            throw new MeasureNotFoundException(creationDateTime);
        }
    }
    
}
