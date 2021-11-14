package com.app.APICode.measure;

import java.util.List;

public interface MeasureService {

    /**
     * Gets all measures in the DB
     * 
     * @return list of {@link Measure}
     */
    List<Measure> listMeasures();

    /**
     * Get a specific measure by its type. 
     * <p> If no Measure is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measureType string containing the type of measure
     * @return a {@link Measure} specified by the provided type
     */
    Measure getMeasure(String measureType);

    /**
     * Adds a new measure to DB.
     * <p> If measure type already exists, throw a {@link MeasureDuplicateException}.
     * 
     * @param measure a new {@link Measure} to be persisted
     * @return the saved {@link Measure}
     */
    Measure addMeasure(Measure measure);

    /**
     * Updates a measure that exists in DB.
     * <p> If no Measure of the measure type is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measure Contains the updated {@link Measure}
     * @return the updated {@link Measure}
     */
    Measure updateMeasure(Measure measure);

    /**
     * Deletes measures based on the provided type.
     * <p> If no Measure of the measure type is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measureType a String containing the measure Type
     */
    void deleteMeasure(String measureType);
}
