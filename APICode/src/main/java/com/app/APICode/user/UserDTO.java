package com.app.APICode.user;

import com.app.APICode.employee.Employee;

import org.springframework.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A Data Transfer Object representing a {@link User} class
 */
public class UserDTO {

    @Schema(description = "Unique identifier of the User.", example = "1", required = true)
    private Long id;

    @Schema(description = "Email of the User.", example = "JohnDoe@abc.com", required = true)
    private String email;

    @Schema(description = "Username of the User.", example = "JohnDoe", required = true)
    private String username;

    @Schema(description = "First Name of the User.", example = "John", required = true)
    private String firstName;

    @Schema(description = "Last Name of the User.", example = "Doe")
    private String lastName;

    @Schema(description = "Role of User.", required = true)
    private String role;

    @Schema(description = "If the user account is enabled.", required = true)
    private boolean isEnabled;

    @Schema(description = "Name of the business User work at.", required = true)
    private String company;

    @Schema(description = "Vaccination Status of User.", required = true, hidden = true)
    private boolean isVaccinated ;

    @Schema(description = "Employee details of User")
    private Employee employee;

    /**
     * Reduce information contained in the User class for transmission
     * 
     * @param user {@link User} class
     * @return a UserDTO
     */
    public static UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.id = user.getId();
        userDTO.email = user.getEmail();
        userDTO.username = user.getUsername();
        userDTO.firstName = user.getFirstName();
        userDTO.lastName = user.getLastName();
        userDTO.isEnabled = user.isEnabled();
        userDTO.isVaccinated = user.getIsVaccinated();
        if (user.getEmployee() != null) {
            userDTO.company = user.getEmployee().getRestaurant().getName();
            userDTO.employee = user.getEmployee();
        }
        userDTO.role = StringUtils.collectionToCommaDelimitedString(user.getAuthorities()).split("_")[1];
        return userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean getIsVaccinated(){
        return this.isVaccinated;
    }

    public void setIsVaccinated(boolean isVaccinated){
        this.isVaccinated = isVaccinated;
    }

    public void setVaccinated(boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
}
