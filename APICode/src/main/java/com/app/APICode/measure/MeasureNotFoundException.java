package com.app.APICode.measure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a {@link Measure} is not found in the repository.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MeasureNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MeasureNotFoundException(String measureType) {
        super("Could not find measure with measure type: " + measureType);
    }
}
