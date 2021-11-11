package com.app.APICode.restaurant;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
@Tag(name = "Restaurant", description = "Restaurant API")
public class RestaurantController {
        private RestaurantService restaurantService;

        public RestaurantController(RestaurantService restaurantService) {
                this.restaurantService = restaurantService;
        }

        /**
         * GET a list of all restaurants
         * 
         * @return list of all {@link Restaurant}
         */
        @Operation(summary = "List all Restaurants", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Restaurant.class)))), })
        @GetMapping("/restaurants")
        public List<RestaurantDTO> getAllRestaurants() {
                return restaurantService.listRestaurants();
        }

        /**
         * GET a restaurant with the given id
         * 
         * @param id of restaurant
         * @return {@link Restaurant} with the given id
         * @throws RestaurantNotFoundException in case a restaurant with the provided
         *                                     {@literal id} does not exist
         */
        @Operation(summary = "Get Restaurant", description = "Get Restaurant by id", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of Restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
                        @ApiResponse(responseCode = "404", description = "Restaurant does not exists"), })
        @GetMapping("/restaurants/{id}")
        public Restaurant getRestaurant(@PathVariable long id) {
                return restaurantService.getRestaurantById(id);
        }

        @Operation(summary = "Get Restaurant managed by User", description = "Get Restaurant that belongs to User according to Credentials", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of Restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
                        @ApiResponse(responseCode = "404", description = "User, Employee and/or Restaurant does not exists"), })
        @GetMapping("/restaurants/user/{username}")
        public Restaurant getRestaurant(@PathVariable(value = "username") String username) {
                return restaurantService.getRestaurantByUsername(username);
        }

        /**
         * Add new restaurant with POST request
         * 
         * @param restaurant {@link Restaurant} containing the info to be added
         * @return the newly added restaurant
         * @throws RestaurantDuplicateException in case a restaurant with the same
         *                                      {@literal id} exist
         */
        @Operation(summary = "Add new restaurant", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "201", description = "Successful created new Restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
                        @ApiResponse(responseCode = "409", description = "Duplicate Restaurant "), })
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/restaurants")
        public Restaurant addRestaurant(@Valid @RequestBody Restaurant restaurant) {
                return restaurantService.addRestaurant(restaurant);
        }

        /**
         * Updates restaurant with PUT request to "/restaurants/{id}"
         * 
         * @param id                of the {@link Restaurant}
         * @param updatedRestaurant {@link Restaurant} containing the updated
         *                          information
         * @return the updated {@link Restaurant}
         * @throws RestaurantNotFoundException in case a restaurant with the provided
         *                                     {@literal id} does not exist
         */
        @Operation(summary = "Update Restaurant information", description = "Updates Restaurants infomation with the provided ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful updated Restaurant information", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Restaurant.class))),
                        @ApiResponse(responseCode = "409", description = "Duplicate Restaurant"), })
        @PutMapping("/restaurants/{id}")
        public Restaurant updateRestaurant(@PathVariable long id, @Valid @RequestBody Restaurant updatedRestaurant) {
                return restaurantService.updateRestaurant(id, updatedRestaurant);
        }

        /**
         * Removes a {@link Restaurant} with DELETE request to "/restaurants/{id}"
         * 
         * @param id of restaurant
         * @throws RestaurantNotFoundException in case a restaurant with the provided
         *                                     {@literal id} does not exist
         */
        @Operation(summary = "Delete Restaurant", security = @SecurityRequirement(name = "bearerAuth"), tags = {
                        "Restaurant" })
        @ApiResponses({ @ApiResponse(responseCode = "204", description = "Successful deleted Restaurant", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Restaurant does not exists"), })
        @DeleteMapping("/restaurants/{id}")
        public void deleteRestaurant(@PathVariable long id) {
                restaurantService.removeById(id);
        }
}
