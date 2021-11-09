package com.app.APICode.measure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Measure measure2 = measure;
        when(measures.findByMeasureType(measure.getMeasureType())).thenReturn(Optional.of(measure));

        // Act
        MeasureDuplicateException duplicateException = assertThrows(MeasureDuplicateException.class, () -> {
            measureService.addMeasure(measure2);
        });

        // Assert
        assertEquals(duplicateException.getMessage(), "This measure " + measure2.getMeasureType() + " already exists. Update measure instead.");
        verify(measures).findByMeasureType(measure.getMeasureType());
        verify(measures, never()).save(measure);
    }

    @Test
    void updateExistingMeasure_ReturnUpdatedMeasure() {
        // Arrange
        Measure measure = new Measure("gym", 50, true, false);
        Measure measure1 = measure;
        measure1.setMaskStatus(true);

        when(measures.findByMeasureType(measure1.getMeasureType())).thenReturn(Optional.of(measure1));
        when(measures.save(any(Measure.class))).thenReturn(measure1);
   
        // Act
        Measure updatedMeasure = measureService.updateMeasure(measure1);

        // Assert
        assertNotNull(updatedMeasure);
        // assertEquals(true,measure.isMaskStatus());

        verify(measures).findByMeasureType(measure.getMeasureType());
        verify(measures).save(measure);
    }

    @Test
    void updateExistingMeasure_NotFound_ReturnNull() {
        // Arrange
        Measure measure = new Measure(null, 40, true, true);
        when(measures.findByMeasureType(measure.getMeasureType())).thenReturn(Optional.empty());

        //Act

        MeasureNotFoundException notFoundException = assertThrows(MeasureNotFoundException.class, () -> {
            measureService.updateMeasure(measure);
        });

        //Assert
        assertEquals(notFoundException.getMessage(),"Could not find measure on: " + measure.getMeasureType());
        
        verify(measures).findByMeasureType(measure.getMeasureType());
    }
}
