package com.app.APICode.measure;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Covid Measures", description = "Covid Measures API")
public class MeasureController {
    private MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    /**
     * List all measures in the system
     * 
     * @return list of all measures
     */
    @Operation(summary = "List all Restaurants", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Covid Measures" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Measure.class)))), })
    @GetMapping("/measures")
    public List<Measure> getMeasures() {
        return measureService.listMeasures();
    }

    /**
     * List the latest measures (by DateTime) in the system
     * 
     * @return list of latest measures
     */
    @Operation(summary = "Get latest Covid Measure", description = "Get latest Covid Measure by DateTime", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Covid Measures" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval of Covid Measure", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Measure.class))) })
    @GetMapping("/measures/latest")
    public Measure getLatestMeasure() {
        return measureService.getLatestMeasure();
    }

    /**
     * Search for the measure given the creationDateTime If there is no measure with
     * the given creationDateTime, throw a MeasureNotFoundException
     * 
     * @param creationDateTime Date object containing the date to be searched
     * @return measure with the given creationDateTime
     */
    @GetMapping("/measures/{creationDateTime}")
    public Measure getMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime) {
        Measure measure = measureService.getMeasure(creationDateTime);

        if (measure == null)
            throw new MeasureNotFoundException(creationDateTime);
        return measureService.getMeasure(creationDateTime);
    }

    /**
     * Add a new measure with POST request to "/measures"
     * 
     * @param measure
     * @return the newly added measure
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/measures")
    public Measure addMeasure(@Valid @RequestBody Measure measure) {
        Measure savedMeasure = measureService.addMeasure(measure);
        return savedMeasure;
    }

    /**
     * Update the info of a measure If there is no measure with the given
     * creationDateTime, throw MeasureNotFoundException
     * 
     * @param creationDateTime the creationDateTime of the measure
     * @param newMeasureInfo   a Measure object containing the new measure info to
     *                         be updated
     * @return the updated Measure object
     */
    @PutMapping("/measures/{creationDateTime}") 
    public Measure updateMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime,
            @Valid @RequestBody Measure newMeasureInfo) {
        Measure measure = measureService.updateMeasure(creationDateTime, newMeasureInfo);
        if (measure == null)
            throw new MeasureNotFoundException(creationDateTime);

        return measure;
    }

    /**
     * Remove a measure with the DELETE request to "/measures/{creationDateTime}" If
     * there is no measure with the given creationDateTime, throw a
     * MeasureNotFoundException
     * 
     * @param creationDateTime the creationDateTime of the measure
     */
    @Operation(summary = "Delete Measures", description = "Delete Covid Measures by the DateTime", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "Covid Measures" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful deleted Covid Measures", content = @Content) })
    @Transactional
    @DeleteMapping("/measures/{creationDateTime}")
    public void deleteMeasure(@PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) Date creationDateTime) {
        try {
            measureService.deleteMeasure(creationDateTime);
        } catch (EmptyResultDataAccessException e) {
            throw new MeasureNotFoundException(creationDateTime);
        }
    }

}
