package com.app.APICode.ctest;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

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
@Tag(name = "COVID-19 Test", description = "COVID-19 Test API")
public class CtestController {
        private CtestService ctests;

        public CtestController(CtestService ctests) {
                this.ctests = ctests;
        }

    /**
     * Search for employee with the given username If there is no user with the
     * given username, throw a UserNotFoundException If there is no employee with
     * the given username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @return list of the ctests done by the employee
     */
    @Operation(summary = "Get all Tests Results", description = "Get all Covid-19 Test results by their credentials", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "COVID-19 Test" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ctest.class)))) })
    @GetMapping("/users/employee/ctests")
    public List<Ctest> getAllTestsByEmployee(Principal principal) {
        return ctests.getAllCtestsByUsername(principal.getName());
    }

    /**
     * Add a new ctest with POST request to "/employee/ctests"
     * 
     * @param username username of employee
     * @param ctest    a new ctest object to be added
     * @return the newly added ctest object
     */
    @Operation(summary = "Add new Test result", description = "Add new Covid-19 Test result by credentials", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "COVID-19 Test" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful created new Covid-19 Test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ctest.class))), })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/employee/ctests")
    public Ctest addCtest(Principal principal, @RequestBody Ctest ctest) {
        return ctests.saveCtestByUsername(principal.getName(), ctest);
    }

    /**
     * Updates the info an employee's ctest If there is no employee with the given
     * username, throw a EmployeeNotFoundException
     * 
     * @param username username of employee
     * @param ctestId  a long value (Ctest)
     * @param newCtest a Ctest object containing the new Ctest info to be updated
     * @return the updated Ctest object
     */
    @Operation(summary = "Update Test result", description = "Update Covid-19 Test result by credentials and provided ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "COVID-19 Test" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful updated  Covid-19 Test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ctest.class))), })
    @PutMapping("/users/employee/ctests/{ctestId}")
    public Ctest updateCtest(Principal principal, @PathVariable(value = "ctestId") Long ctestId,
            @RequestBody Ctest newCtest) {
        return ctests.updateCtestByCtestIdAndUsername(principal.getName(), ctestId, newCtest);
    }

    /**
     * Remove a Ctest with the DELETE request to
     * "/employee/{username}/ctests/{ctestId}" If there is no user with the given
     * "username" throw a UserNotFoundException If there is no employee with the
     * user, throw a EmployeeNotFoundException If there is no Ctest with the given
     * "id", throw a CtestNotFoundException
     * 
     * @param username username of employee
     * @param ctestId  a long value (Ctest)
     */
    @Operation(summary = "Delete Test result", description = "Delete Covid-19 Test result by credentials and provided ID", security = @SecurityRequirement(name = "bearerAuth"), tags = {
            "COVID-19 Test" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful deleted Covid-19 Test result", content = @Content) })
    @DeleteMapping("/users/employee/ctests/{ctestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCtest(Principal principal, @PathVariable(value = "ctestId") Long ctestId) {
        ctests.deleteCtestByCtestIdAndUsername(principal.getName(), ctestId);
    }

    /**
     * Get the next Covid test date for the employee based on the restraunts test frequency
     *  and the employees latest covid test date 
     * Username and not principal.getName() is used as we want business owners to be able to get this informaiton as well
     * "/employee/{username}/ctests/{ctestId}" 
     * If there is no user with the given "username" throw a UserNotFoundException 
     * If there is no employee with the user, throw a EmployeeNotFoundException 
     * If there is no Ctests with the given "id", throw a CtestNotFoundException
     * if Ctests is empty it would return todays date
     * 
     * @param username username of employee
     */
    @Operation(summary = "Get Next Covid Test date", description = "Get the next covid testing date using the usernam eof the employee", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "COVID-19 Test" })
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Successful retrieved next covid test date", content = @Content) })
        @GetMapping("/users/employee/ctests/next")
        public Date getNextCtest(Principal principal) {
                return ctests.getNextCtestByUsername(principal.getName());
        }

     /**
     * List latest ctests of employees in a particular business
     * 
     * @param principal name of the user logged in currently
     * @return list of ctest of employees in a particular business
     */
    @Operation(summary = "List all Ctests", description = "List all ctests of employees from the Restuarant that is owned by the User", security = @SecurityRequirement(name = "bearerAuth"), tags = {
        "Employee" })
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Successful Retrieval", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ctest.class)))), })
        @ResponseStatus(HttpStatus.OK)
        @GetMapping("/users/employee/allctests")
        public List<Ctest> getAllEmployeesCtest(Principal principal) {
                return ctests.getAllEmployeesCtest(principal.getName());
        }
}
