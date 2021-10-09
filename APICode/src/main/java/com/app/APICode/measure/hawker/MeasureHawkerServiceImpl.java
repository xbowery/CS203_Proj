package com.app.APICode.measure.hawker;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MeasureHawkerServiceImpl implements MeasureHawkerService{
    private MeasureHawkerRepository measureHawkers;

    //for testing purposes 
    public MeasureHawkerServiceImpl(MeasureHawkerRepository measureHawkers) {
        this.measureHawkers = measureHawkers;
        measureHawkers.save(new MeasureHawker(new Date(), 2, true));
        measureHawkers.save(new MeasureHawker(new Date(), 5, false));

    }

    @Override
    public List<MeasureHawker> listMeasureHawkers() {
        return measureHawkers.findAll();
    }

    @Override 
    public MeasureHawker getMeasureHawker(Date creationDateTime){
        return measureHawkers.findByCreationDateTime(creationDateTime).orElse(null);
    }

    @Override
    public MeasureHawker addMeasureHawker(MeasureHawker measureHawker) {
        List<MeasureHawker> sameMeasureHawker = measureHawkers.findByCreationDateTime(measureHawker.getCreationDateTime())
        .map(Collections::singletonList)
        .orElseGet(Collections::emptyList);

        if (sameMeasureHawker.size() == 0) {
            return measureHawkers.save(measureHawker);
        } else {
            return null;
        }
    }

    @Override
    public MeasureHawker updateMeasureHawker(Date creationDateTime, MeasureHawker newMeasureHawker) {
        return measureHawkers.findByCreationDateTime(creationDateTime).map(measureHawker -> {
            measureHawker.setCreationDateTime(newMeasureHawker.getCreationDateTime());
                measureHawker.setMaxPerTable(newMeasureHawker.getMaxPerTable());
                measureHawker.setVaccinationStatus(newMeasureHawker.isVaccinationStatus());
                return measureHawkers.save(measureHawker);
        }).orElse(null);
    }

    @Override 
    public void deleteMeasureHawker(Date creationDateTime) {
        measureHawkers.deleteByCreationDateTime(creationDateTime);
    }
}
