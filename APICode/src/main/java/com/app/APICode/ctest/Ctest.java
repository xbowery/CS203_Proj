package com.app.APICode.ctest;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.app.APICode.employee.Employee;
import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Ctest {
    @Schema(description = "Unique identifier of the Covid-19 Test Result.", example = "1", required = true)
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    // One test result has only one user linked to it
    // but one user can have multiple tests
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    @Schema(description = "Employee that this Covid-19 Test belongs to.", required = true)
    private Employee employee;

    @NotNull(message = "type of test should not be null")
    @Pattern(regexp = "(PCR|ART)")
    @Schema(description = "Type of Covid-19 Test.", example = "PCR", required = true)
    private String type;

    @NotNull(message = "date should not be null")
    @Schema(description = "Date Covid-19 Test was taken.", required = true)
    private Date date;

    @NotNull(message = "result should not be null")
    @Pattern(regexp = "(Positive|Negative|Pending)")
    @Schema(description = "Result of Covid-19 Test.", required = true)
    private String result;

    public Ctest(String type, Date date, String result) {
        this.type = type;
        this.date = date;
        this.result = result;
    }

    public Ctest() {

    }

    public Long getId() {
        return id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
