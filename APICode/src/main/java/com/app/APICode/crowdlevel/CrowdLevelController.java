package com.app.APICode.crowdlevel;

import java.security.Principal;
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
     * Search for the crowd level of a restaurant with the given restaurant id
     * 
     * @param id restaurant id
     * @return crowd level of restaurant
     */
    @GetMapping("/restaurants/crowdLevel")
    public List<CrowdLevel> getCrowdLevelByRestaurant(Principal principal) {
        return crowdLevelService.listCrowdLevelByEmployee(principal.getName());
    }

    /**
     * Add a new crowd level with POST request to "/restaurant/{id}/crowdLevel"
     * 
     * @param id restaurant id
     * @return the newly added CrowdLevel object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/crowdLevel")
    public CrowdLevel addCrowdLevel(Principal principal, @Valid @RequestBody CrowdLevel crowdLevel) {
        return crowdLevelService.addCrowdLevel(principal.getName(), crowdLevel);
    }
}


