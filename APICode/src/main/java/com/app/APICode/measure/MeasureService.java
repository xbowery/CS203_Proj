package com.app.APICode.measure;

import java.util.List;

public interface MeasureService {

    /**
     * Gets all measures in the database
     * 
     * @return list of {@link Measure}
     */
    List<Measure> listMeasures();

    /**
     * Get a specific {@link Measure} by its type. 
     * <p> If no {@link Measure} is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measureType string containing the type of measure
     * @return a {@link Measure} specified by the provided type
     */
    Measure getMeasure(String measureType);

    /**
     * Adds a new {@link Measure} to the database.
     * <p> If measure type already exists, throw a {@link MeasureDuplicateException}.
     * 
     * @param measure a new {@link Measure} to be persisted
     * @return the saved {@link Measure}
     */
    Measure addMeasure(Measure measure);

    /**
     * Updates a {@link Measure} that exists in the database.
     * <p> If no {@link Measure} of the measure type is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measure Contains the updated {@link Measure}
     * @return the updated {@link Measure}
     */
    Measure updateMeasure(Measure measure);

    /**
     * Deletes measures based on the provided type.
     * <p> If no {@link Measure} of the measure type is found, throw a {@link MeasureNotFoundException}.
     * 
     * @param measureType a String containing the measure Type
     */
    void deleteMeasure(String measureType);
}
