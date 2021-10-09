package com.app.APICode.measure.hawker;

import java.util.Date;

public class MeasureHawkerNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MeasureHawkerNotFoundException(Date creationDateTime) {
        super("Could not find measure created on: " + creationDateTime);
    }
}
