package com.app.APICode.measure;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
        @Operation(summary = "List all Measures", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Covid Measures" })
        @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Measure.class)))), })
        @GetMapping("/measures")
        public List<Measure> getAllMeasures() {
                return measureService.listMeasures();
        }

        /**
         * List all measures according to a measure type
         * 
         * @param measureType
         * @return list of all measures under the measure type
         */
        @Operation(summary = "List specific Measure(s)", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Covid Measures" })
        @ApiResponses({ 
                @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Measure.class)))), 
                @ApiResponse(responseCode = "404", description = "Measure does not exist", content = @Content), 
})
        @GetMapping("/measures/{measureType}")
        public Measure getMeasure(@PathVariable @NotNull String measureType) {
                return measureService.getMeasure(measureType);
        }

        /**
         * Add a new measure with POST request to "/measures"
         * 
         * @param measure
         * @return the newly added measure
         */
        @Operation(summary = "Add new measure", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Covid Measures" })
        @ApiResponses({ 
                @ApiResponse(responseCode = "201", description = "Successful created new Measure", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Measure.class))), 
                @ApiResponse(responseCode = "409", description = "Conflicting Measure", content = @Content), 
        })
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/measures")
        public Measure addMeasure(@Valid @RequestBody Measure measure) {
                return measureService.addMeasure(measure);
        }

        /**
         * Update the info of a measure If there is no measure with the given
         * creationDateTime, throw MeasureNotFoundException
         * 
         * @param updatedMeasure a Measure object containing the new measure info to be
         * @return the updated Measure object
         */
        @Operation(summary = "Update Measures information", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Covid Measures" })
        @ApiResponses({ 
                @ApiResponse(responseCode = "200", description = "Successful updated Measures information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Measure.class))), 
                @ApiResponse(responseCode = "404", description = "Measure does not exist", content = @Content), 
        })
        @PutMapping("/measures")
        public Measure updateMeasure(@Valid @RequestBody Measure updatedMeasure) {
                return measureService.updateMeasure(updatedMeasure);
        }

        /**
         * Remove a measure with the DELETE request to "/measures/{measureType}" If
         * there is no measure with the given creationDateTime, throw a
         * MeasureNotFoundException
         * 
         * @param measureType the type of the measure
         */
        @Operation(summary = "Delete Measures", description = "Delete Covid Measures by the Type", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Covid Measures" })
        @ApiResponses({ 
                @ApiResponse(responseCode = "204", description = "Successful deleted Covid Measures", content = @Content), 
                @ApiResponse(responseCode = "404", description = "Measure does not exist", content = @Content), 
        })
        @Transactional
        @DeleteMapping("/measures/{measureType}")
        public void deleteMeasure(@PathVariable @NotNull String measureType) {
                measureService.deleteMeasure(measureType);
        }

}
