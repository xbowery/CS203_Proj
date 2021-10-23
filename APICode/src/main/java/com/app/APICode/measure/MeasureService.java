package com.app.APICode.measure;

import java.util.Date;
import java.util.List;

public interface MeasureService {
    List<Measure> listMeasures();
    Measure getLatestMeasure();
    Measure getMeasure(Date creationDateTime);
    Measure addMeasure(Measure measure);
    Measure updateMeasure(Date creationDateTime, Measure measure);
    void deleteMeasure(Date creationDateTime);
}
