package com.app.APICode.measure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeasureDuplicateException extends RuntimeException {
    public MeasureDuplicateException(String measureType) {
        super("This measure " + measureType + " already exists. Update measure instead.");
    }
}
