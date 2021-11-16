package com.app.APICode.measure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is a same {@link Measure} found in the repository
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class MeasureDuplicateException extends RuntimeException {
    public MeasureDuplicateException(String measureType) {
        super("This measure " + measureType + " already exists. Update measure instead.");
    }
}
