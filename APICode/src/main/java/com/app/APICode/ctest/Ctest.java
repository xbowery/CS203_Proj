package com.app.APICode.ctest;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;

import com.app.APICode.employee.Employee;

@Entity
public class Ctest {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    // One test result has only one user linked to it
    // but one user can have multiple tests
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "type of test should not be null")
    @Pattern(regexp = "(PCR|ART)")
    private String type;

    @NotNull(message = "date should not be null")
    private Date date;

    @NotNull(message = "result should not be null")
    @Pattern(regexp = "(Positive|Negative)")
    private String result;

    public Ctest (String type, Date date, String result){
        this.type = type;
        this.date = date;
        this.result = result;
    }
    public Ctest(){
        
    }

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    public String getResult(){
        return result;
    }
    public void setResult(String result){
        this.result = result;
    }

}
