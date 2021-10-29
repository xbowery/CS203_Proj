package com.app.APICode.measure;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.app.APICode.measure.Measure;
import com.app.APICode.measure.MeasureRepository;
import com.app.APICode.measure.MeasureServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MeasureServiceTest {
    
    @Mock
    private MeasureRepository measures;

    @InjectMocks
    private MeasureServiceImpl measureService;

    @Test
    void addNewMeasure_ReturnSavedMeasure() {
        // Arrange
        Measure measure = new Measure("gym", 50, true, false);

        when(measures.findByMeasureType(any(String.class))).thenReturn(Optional.empty());
        when(measures.save(any(Measure.class))).thenReturn(measure);

        // Act
        Measure savedMeasure = measureService.addMeasure(measure);

        // Assert
        assertNotNull(savedMeasure);
        verify(measures).findByMeasureType(measure.getMeasureType());
        verify(measures).save(measure);
    }

    @Test
    void addExistingMeasure_ReturnNull() {
        // Arrange
        Measure measure = new Measure("gym", 50, true, false);
        
        when(measures.findByMeasureType(measure.getMeasureType())).thenReturn(Optional.of(measure));

        // Act
        Measure savedMeasure = null;
        
        try {
            savedMeasure = measureService.addMeasure(measure);
        } catch (MeasureDuplicateException e) {
            
        }

        // Assert
        assertNull(savedMeasure);
        verify(measures).findByMeasureType(measure.getMeasureType());
        verify(measures, never()).save(measure);
    }
}
