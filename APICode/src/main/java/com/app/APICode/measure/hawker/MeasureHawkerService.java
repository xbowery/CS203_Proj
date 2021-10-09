package com.app.APICode.measure.hawker;

import java.util.Date;
import java.util.List;

public interface MeasureHawkerService {
    List<MeasureHawker> listMeasureHawkers();
    MeasureHawker getMeasureHawker(Date creationDateTime);
    MeasureHawker addMeasureHawker(MeasureHawker measureHawker);
    MeasureHawker updateMeasureHawker(Date creationDateTime, MeasureHawker measureHawker);
    void deleteMeasureHawker(Date creationDateTime);
}
