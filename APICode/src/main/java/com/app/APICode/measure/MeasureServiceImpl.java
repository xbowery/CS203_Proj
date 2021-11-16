package com.app.APICode.measure;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for the {@link Measure} service layer
 */
@Service
public class MeasureServiceImpl implements MeasureService {
    private MeasureRepository measures;

    // for testing purposes
    public MeasureServiceImpl(MeasureRepository measures) {
        this.measures = measures;
    }

    @Override
    public List<Measure> listMeasures() {
        return measures.findAll();
    }

    @Override
    public Measure getMeasure(String measureType) {
        Measure measure = measures.findByMeasureType(measureType).orElse(null);
        
        if (measure == null) 
            throw new MeasureNotFoundException(measureType);

        return measure;
    }

    @Override
    public Measure addMeasure(Measure measure) {
        Measure duplicate = measures.findByMeasureType(measure.getMeasureType()).orElse(null);

        if (duplicate == null) 
            return measures.save(measure);
        
        throw new MeasureDuplicateException(measure.getMeasureType());
    }

    @Override
    public Measure updateMeasure(Measure updatedMeasure) {
        Measure measure = measures.findByMeasureType(updatedMeasure.getMeasureType()).orElse(null);

        if (measure == null)
            throw new MeasureNotFoundException(updatedMeasure.getMeasureType());
            
        measure.setDateUpdated(LocalDate.now());
        measure.setMeasureType(updatedMeasure.getMeasureType());
        measure.setMaxCapacity(updatedMeasure.getMaxCapacity());
        measure.setVaccinationStatus(updatedMeasure.isVaccinationStatus());
        measure.setMaskStatus(updatedMeasure.isMaskStatus());
        return measures.save(measure);
    }

    @Override
    @Transactional
    public void deleteMeasure(String measureType) {
        if (measures.existsByMeasureType(measureType)) {
            measures.deleteByMeasureType(measureType);
            return;
        }
        throw new MeasureNotFoundException(measureType);
    }

}
