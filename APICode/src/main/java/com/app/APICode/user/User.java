package com.app.APICode.user;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.app.APICode.employee.Employee;
import com.app.APICode.verificationtoken.VerificationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the User.", example = "1", required = true, hidden = true)
    private Long id;

    @Email
    @Column(unique = true)
    @NotNull(message = "Email must not be null")
    @Schema(description = "Email of the User.", example = "JohnDoe@abc.com", required = true)
    private String email;

    @NotNull(message = "Username must not be null")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Schema(description = "Username of the User.", example = "JohnDoe", required = true)
    private String username;

    @NotNull(message = "First name must not be null")
    @Schema(description = "First Name of the User.", example = "John", required = true)
    private String firstName;

    @Schema(description = "Last Name of the User.", example = "Doe")
    private String lastName;

    @NotNull(message = "Password must not be null")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    @Schema(description = "Password of the User.", required = true)
    private String password;

    @NotNull(message = "Vaccination status must not be null")
    @Schema(description = "Vaccination Status of User.", required = true, hidden = true)
    private boolean isVaccinated = false;

    @NotNull(message = "Authorities must not be null")
    @Schema(description = "Role of User.", required = true, hidden = true)
    private String authorities = UserRole.USER.role;

    @Schema(description = "If the user account is enabled.", required = true, hidden = true)
    private boolean enabled = false;
    
    @Schema(description = "If the user account has expired.", required = true, hidden = true)
    private boolean accountNonExpired = false;

    @Schema(description = "If the user account is locked.", required = true, hidden = true)
    private boolean accountNonLocked = false;

    @Schema(description = "If the user credential has expired.", required = true, hidden = true)
    private boolean credentialsNonExpired = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Schema(description = "Employee details of User.")
    private Employee employee;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken vToken;

    public User() {
    }

    public User(String email, String username, String firstName, String lastName, String password, boolean isVaccinated,
            String authorities) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isVaccinated = isVaccinated;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return this.id;
    }

    @Override
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsVaccinated() {
        return isVaccinated;
    }

    public void setIsVaccinated(boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
