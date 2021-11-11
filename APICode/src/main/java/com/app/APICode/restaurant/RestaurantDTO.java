package com.app.APICode.restaurant;

public class RestaurantDTO {

    private long id;
    private String name;
    private String location;
    private String cuisine;
    private String description;
    private int maxCapacity;
    private String crowdLevel;
    private String imageUrl;

    /**
     * Reduce information contained in the Restaurant class for transmission
     * 
     * @param restaurant {@link Restaurant} class
     * @return a RestaurantDTO
     */
    public static RestaurantDTO convertToRestaurantDTO (Restaurant restaurant) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.id = restaurant.getId();
        restaurantDTO.name = restaurant.getName();
        restaurantDTO.location = restaurant.getLocation();
        restaurantDTO.cuisine = restaurant.getCuisine();
        restaurantDTO.description = restaurant.getDescription();
        restaurantDTO.maxCapacity = restaurant.getMaxCapacity();
        restaurantDTO.crowdLevel = restaurant.getcurrentCrowdLevel();
        restaurantDTO.imageUrl = restaurant.getImageUrl();
        return restaurantDTO;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getCuisine() {
        return cuisine;
    }
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getMaxCapacity() {
        return this.maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    public String getcurrentCrowdLevel() {
        return crowdLevel;
    }
    public void setcurrentCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
