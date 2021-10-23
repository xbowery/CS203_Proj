package com.app.APICode.measure;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeasureServiceImpl implements MeasureService{
    private MeasureRepository measures;

    //for testing purposes
    public MeasureServiceImpl(MeasureRepository measures) {
        this.measures = measures;
        measures.save(new Measure(new Date(), "restaurant", 50, true, false, null));
    } 

    @Override
    public List<Measure> listMeasures() {
        return measures.findAll();
    }

    @Override
    public Measure getLatestMeasure(){
        return measures.findTopByOrderByCreationDateTimeDesc();
    }

    @Override
    public Measure getMeasure(Date creationDateTime) {
        return measures.findByCreationDateTime(creationDateTime).orElse(null);
    }

    @Override
    public Measure addMeasure(Measure measure) {
        List<Measure> sameMeasure = measures.findByCreationDateTime(measure.getCreationDateTime())
                .map(Collections::singletonList).orElseGet(Collections::emptyList);

        if (sameMeasure.size() == 0) {
            return measures.save(measure);
        } else {
            return null;
        }
    }

    @Override
    public Measure updateMeasure(Date creationDateTime, Measure newMeasure) {
        return measures.findByCreationDateTime(creationDateTime).map(measure -> {
            measure.setCreationDateTime(newMeasure.getCreationDateTime());
            measure.setBusinessType(newMeasure.getBusinessType());
            measure.setMaxCapacity(newMeasure.getMaxCapacity());
            measure.setVaccinationStatus(newMeasure.isVaccinationStatus());
            measure.setMaskStatus(newMeasure.isMaskStatus());
            measure.setDetails(newMeasure.getDetails());
            return measures.save(measure);
        }).orElse(null);
    }

    @Override
    public void deleteMeasure(Date creationDateTime) {
        measures.deleteByCreationDateTime(creationDateTime);
    }

}
