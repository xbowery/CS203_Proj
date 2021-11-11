package com.app.APICode.crowdlevel;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Crowd Level", description = "Crowd Level API")
public class CrowdLevelController {
    private CrowdLevelService crowdLevelService;

    @Autowired
    public CrowdLevelController(CrowdLevelService crowdLevelService) {
        this.crowdLevelService = crowdLevelService;
    }

    /**
     * List the latest crowd levels by datetime and restaurant in the system
     * @return list of latest crowd levels
     */
    @Operation(summary = "Get all restaurants latest crowd levels", description = "Get all latest crowd levels for all restaurants", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))) })
    @GetMapping("restaurants/crowdLevels")
    public List<CrowdLevel> getCrowdLevels(){
        return crowdLevelService.listAllCrowdLevels();
    }

    /**
     * Search for the crowd level of a restaurant with the given restaurant id
     * 
     * @param id restaurant id
     * @return crowd level of restaurant
     */
    @Operation(summary = "Get the specific restaurants crowd levels", description = "Get all crowd levels for one specific restaurants by the specified restaurant", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))) })
    @GetMapping("/restaurants/{username}/crowdLevel")
    public List<CrowdLevel> getCrowdLevelByRestaurant(@PathVariable (value = "username") String username) {
        return crowdLevelService.listCrowdLevelByEmployee(username);
    }

    /**
     * Add a new crowd level with POST request to "/restaurant/{id}/crowdLevel"
     * 
     * @param id restaurant id
     * @return the newly added CrowdLevel object
     */
    @Operation(summary = "Add a new crowd level object", description = "Add a new crowd level entry by restaurant ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successful created new Crowd Level object", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))) })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/{id}/crowdLevel")
    public CrowdLevel addCrowdLevel(@PathVariable Long id, @Valid @RequestBody CrowdLevel crowdLevel) {
        return crowdLevelService.addCrowdLevel(id, crowdLevel);
    }

    /**
     * Update crowd level
     * If there is no crowd level with the given datetime, throw CrowdLevelNotFoundException
     * @param crowdLevelDateTime the datetime of the measure
     * @param newCrowdLevel a CrowdLevel object containing the new crowd level to be updated
     * @return the updated CrowdLevel object
     */
    @Operation(summary = "Update the crowd level object", description = "Update the specific crowd level object by provided ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful updated Crowd Level object", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))) })
    @PutMapping("/restaurants/{id}/crowdLevel/{crowdLevelId}")
    public CrowdLevel updateCrowdLevel(@PathVariable Long id,
    @PathVariable Long crowdLevelId, @RequestBody CrowdLevel newCrowdLevel){
        return crowdLevelService.updateCrowdLevel(id, crowdLevelId, newCrowdLevel);
    }

}


