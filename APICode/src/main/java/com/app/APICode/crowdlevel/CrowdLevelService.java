package com.app.APICode.crowdlevel;

import java.util.List;

import com.app.APICode.restaurant.Restaurant;
import com.app.APICode.user.User;
import com.app.APICode.user.UserForbiddenException;

public interface CrowdLevelService {

    /**
     * Retrieves a list of {@link CrowdLevel} with the specified business's
     * owner "username".
     * 
     * If associated {@link User} is not a Business owner, throw a {@link UserForbiddenException}.
     *  
     * @param username a string containing the Business owner's username
     * @return a list of {@link CrowdLevel}
     */
    List<CrowdLevel> listCrowdLevelByEmployee(String username);

    /**
     * Creates a {@link CrowdLevel} object for the {@link Restaurant} with specified "username".
     * 
     * @param username     a string containing the username of the restaurant
     * @param crowdLevel   a CrowdLevel object
     * @return a newly added {@link CrowdLevel}
     */
    CrowdLevel addCrowdLevel(String username, CrowdLevel crowdLevel);
}
