package com.app.APICode.employee.message;

import javax.validation.constraints.NotNull;

/**
 * Class containing the attributes for the JSON data for a user requesting to be an employee
 */
public class RequestMessage {
    
    @NotNull
    private String designation;
    
    @NotNull
    private long restaurantId;
    
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
