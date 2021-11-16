package com.app.APICode.crowdlevel;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

/**
 * Rest Controller that manages HTTP requests and updates data within {@link CrowdLevelService}
 */
@RestController
@Tag(name = "Crowd Level", description = "Crowd Level API")
public class CrowdLevelController {
    private CrowdLevelService crowdLevelService;

    @Autowired
    public CrowdLevelController(CrowdLevelService crowdLevelService) {
        this.crowdLevelService = crowdLevelService;
    }

    /**
     *
     * Search for the crowd level of a restaurant with the given restaurant id
     *
     * @param principal {@link Principal} object containing the username of the user logged in currently
     * @return crowd level of restaurant
     */
    @Operation(summary = "Get the specific restaurants crowd levels", description = "Get all crowd levels for one specific restaurants by the specified restaurant", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))),
        @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Employee does not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Restaurant does not exist", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cannot find CrowdLevel with the following Restaurant name", content = @Content)
    })
    @GetMapping("/restaurants/crowdLevel")
    public List<CrowdLevel> getCrowdLevelByRestaurant(Principal principal) {
        return crowdLevelService.listCrowdLevelByEmployee(principal.getName());
    }

    /**
     * Add a new crowd level with POST request to "/restaurant/{id}/crowdLevel"
     * 
     * @param principal {@link Principal} object containing the username of the user logged in currently
     * @return the newly added CrowdLevel object
     */
    @Operation(summary = "Add a new crowd level object", description = "Add a new crowd level entry by restaurant ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Crowd Levels" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successful created new Crowd Level object", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CrowdLevel.class)))),
        @ApiResponse(responseCode = "404", description = "Restaurant does not exist", content = @Content) 
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/crowdLevel")
    public CrowdLevel addCrowdLevel(Principal principal, @Valid @RequestBody CrowdLevel crowdLevel) {
        return crowdLevelService.addCrowdLevel(principal.getName(), crowdLevel);
    } 
}


