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

import io.swagger.v3.oas.annotations.tags.Tag;

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
    @GetMapping("/restaurants/crowdLevels")
    public List<CrowdLevel> getCrowdLevels(){
        return crowdLevelService.listAllCrowdLevels();
    }

    /**
     * Search for the crowd level of a restaurant with the given restaurant id
     * 
     * @param id restaurant id
     * @return crowd level of restaurant
     */
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
    @PutMapping("/restaurants/{id}/crowdLevel/{crowdLevelId}")
    public CrowdLevel updateCrowdLevel(@PathVariable Long id,
    @PathVariable Long crowdLevelId, @RequestBody CrowdLevel newCrowdLevel){
        return crowdLevelService.updateCrowdLevel(id, crowdLevelId, newCrowdLevel);
    }

}


